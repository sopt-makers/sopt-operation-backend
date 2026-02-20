#!/bin/bash

# =============================================================================
# SOPT Operation Backend - Lambda 로컬 배포 테스트 스크립트
# =============================================================================
# 사용법:
#   ./lambda/deploy-test.sh        # dev 환경 배포 (기본값)
#   ./lambda/deploy-test.sh dev    # dev 환경 배포
#   ./lambda/deploy-test.sh prod   # prod 환경 배포
# =============================================================================

set -e  # 에러 발생 시 즉시 중단

# -----------------------------------------------------------------------------
# 설정
# -----------------------------------------------------------------------------
ENV=${1:-dev}
S3_BUCKET="operation-lambda-deploy"
STACK_NAME="sopt-operation-${ENV}"
AWS_REGION="ap-northeast-2"

# 스크립트 디렉토리
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# -----------------------------------------------------------------------------
# 함수 정의
# -----------------------------------------------------------------------------
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# -----------------------------------------------------------------------------
# 메인 스크립트
# -----------------------------------------------------------------------------
echo ""
echo "=============================================="
echo "  SOPT Operation Lambda 배포 (환경: ${ENV})"
echo "=============================================="
echo ""

# 0. 프로젝트 루트로 이동
cd "$PROJECT_ROOT"
log_info "프로젝트 루트: ${PROJECT_ROOT}"

# 1. 환경변수 파일 확인 및 로드
ENV_FILE="${SCRIPT_DIR}/.env"
if [ ! -f "$ENV_FILE" ]; then
    log_error "환경변수 파일이 없습니다: ${ENV_FILE}"
    echo ""
    echo "  다음 명령어로 환경변수 파일을 생성하세요:"
    echo "  cp lambda/.env.example lambda/.env"
    echo "  그리고 실제 값을 입력하세요."
    exit 1
fi

log_info "환경변수 로드 중: ${ENV_FILE}"
set -a  # export all variables
source "$ENV_FILE"
set +a
log_success "환경변수 로드 완료"

# 2. AWS 자격 증명 확인
log_info "AWS 자격 증명 확인 중..."
if ! aws sts get-caller-identity > /dev/null 2>&1; then
    log_error "AWS 자격 증명이 설정되지 않았습니다."
    echo "  → aws configure 또는 AWS_PROFILE 환경변수를 설정해주세요."
    exit 1
fi

AWS_ACCOUNT=$(aws sts get-caller-identity --query Account --output text)
log_success "AWS Account: ${AWS_ACCOUNT}"

# 3. S3 버킷 확인
log_info "S3 버킷 확인 중... (${S3_BUCKET})"
if ! aws s3 ls "s3://${S3_BUCKET}" > /dev/null 2>&1; then
    log_warning "S3 버킷이 존재하지 않습니다. 생성합니다..."
    aws s3 mb "s3://${S3_BUCKET}" --region ${AWS_REGION}
    log_success "S3 버킷 생성 완료: ${S3_BUCKET}"
else
    log_success "S3 버킷 확인 완료: ${S3_BUCKET}"
fi

# 4. 기존 실패한 스택 삭제 (ROLLBACK_COMPLETE 상태인 경우)
log_info "기존 스택 상태 확인 중..."
STACK_STATUS=$(aws cloudformation describe-stacks \
    --stack-name "${STACK_NAME}" \
    --query "Stacks[0].StackStatus" \
    --output text \
    --region ${AWS_REGION} 2>/dev/null || echo "NOT_FOUND")

if [ "$STACK_STATUS" = "ROLLBACK_COMPLETE" ]; then
    log_warning "실패한 스택 발견 (ROLLBACK_COMPLETE). 삭제 중..."
    aws cloudformation delete-stack --stack-name "${STACK_NAME}" --region ${AWS_REGION}
    aws cloudformation wait stack-delete-complete --stack-name "${STACK_NAME}" --region ${AWS_REGION}
    log_success "스택 삭제 완료"
fi

# 5. Lambda JAR 빌드
log_info "Lambda JAR 빌드 중..."
./gradlew clean :operation-api:lambdaJar -x test --quiet

# 빌드된 파일 찾기
JAR_FILE=$(ls operation-api/build/distributions/*-lambda.zip 2>/dev/null | head -1)
if [ -z "$JAR_FILE" ]; then
    log_error "Lambda ZIP 파일을 찾을 수 없습니다."
    exit 1
fi

JAR_SIZE=$(du -h "$JAR_FILE" | cut -f1)
log_success "빌드 완료: ${JAR_FILE} (${JAR_SIZE})"

# 6. S3 업로드
TIMESTAMP=$(date +"%Y%m%d-%H%M%S")
S3_KEY="deploy/${STACK_NAME}-${TIMESTAMP}.zip"

log_info "S3 업로드 중..."
echo "  → 파일: ${JAR_FILE}"
echo "  → 경로: s3://${S3_BUCKET}/${S3_KEY}"

aws s3 cp "$JAR_FILE" "s3://${S3_BUCKET}/${S3_KEY}" --quiet
log_success "S3 업로드 완료"

# 7. SAM 배포
log_info "SAM 배포 중... (Stack: ${STACK_NAME})"
cd "$SCRIPT_DIR"

sam deploy \
    --template "template-${ENV}.yaml" \
    --stack-name "${STACK_NAME}" \
    --region "${AWS_REGION}" \
    --capabilities CAPABILITY_IAM \
    --no-fail-on-empty-changeset \
    --no-confirm-changeset \
    --parameter-overrides \
        S3Bucket="${S3_BUCKET}" \
        S3Key="${S3_KEY}" \
        SpringProfile="lambda-${ENV}" \
        DbDriverClassName="${DB_DRIVER_CLASS_NAME}" \
        DbUrl="${DB_URL}" \
        DbUsername="${DB_USERNAME}" \
        DbPassword="${DB_PASSWORD}" \
        JwtSecretKeyApp="${JWT_SECRET_KEY_APP}" \
        JwtSecretKeyAccess="${JWT_SECRET_KEY_ACCESS}" \
        JwtSecretKeyRefresh="${JWT_SECRET_KEY_REFRESH}" \
        JwtSecretPlatformCode="${JWT_SECRET_PLATFORM_CODE}" \
        SecretKeyPg="${SECRET_KEY_PG}" \
        SoptCurrentGeneration="${SOPT_CURRENT_GENERATION}" \
        SoptMakerPgUrl="${SOPT_MAKER_PG_URL}" \
        SoptMakerPgToken="${SOPT_MAKER_PG_TOKEN}" \
        SoptAlarmMessageTitle="${SOPT_ALARM_MESSAGE_TITLE}" \
        SoptAlarmMessageContent="${SOPT_ALARM_MESSAGE_CONTENT}" \
        AdminUrlProd="${ADMIN_URL_PROD}" \
        AdminUrlDev="${ADMIN_URL_DEV}" \
        AdminUrlLocal="${ADMIN_URL_LOCAL}" \
        NotificationUrl="${NOTIFICATION_URL}" \
        NotificationKey="${NOTIFICATION_KEY}" \
        NotificationArn="${NOTIFICATION_ARN}" \
        AwsCredentialsAccessKey="${AWS_CREDENTIALS_ACCESS_KEY}" \
        AwsCredentialsSecretKey="${AWS_CREDENTIALS_SECRET_KEY}" \
        AwsCredentialsEventBridgeRoleArn="${AWS_CREDENTIALS_EVENTBRIDGE_ROLE_ARN}" \
        AwsRegion="${AWS_REGION}" \
        BucketForBanner="${BUCKET_FOR_BANNER}" \
        ApkKey="${APK_KEY}" \
        MakersAuthJwkEndpoint="${MAKERS_AUTH_JWK_ENDPOINT}" \
        MakersAuthJwkIssuer="${MAKERS_AUTH_JWK_ISSUER}" \
        AuthApiKey="${AUTH_API_KEY}" \
        OurServiceName="${OUR_SERVICE_NAME}"

cd "$PROJECT_ROOT"

# 8. 결과 출력
log_success "배포 완료!"
echo ""
echo "=============================================="
echo "  배포 결과"
echo "=============================================="

# API 엔드포인트 조회
API_ENDPOINT=$(aws cloudformation describe-stacks \
    --stack-name "${STACK_NAME}" \
    --query "Stacks[0].Outputs[?OutputKey=='ApiEndpoint'].OutputValue" \
    --output text \
    --region "${AWS_REGION}" 2>/dev/null)

LAMBDA_NAME=$(aws cloudformation describe-stacks \
    --stack-name "${STACK_NAME}" \
    --query "Stacks[0].Outputs[?OutputKey=='LambdaFunctionName'].OutputValue" \
    --output text \
    --region "${AWS_REGION}" 2>/dev/null)

echo ""
echo -e "${GREEN}🌐 API Endpoint:${NC} ${API_ENDPOINT}"
echo -e "${GREEN}🔧 Lambda Function:${NC} ${LAMBDA_NAME}"
echo ""
echo "테스트 명령어:"
echo "  curl ${API_ENDPOINT}/api/v1/test/health"
echo ""
echo "CloudWatch 로그:"
echo "  aws logs tail /aws/lambda/${LAMBDA_NAME} --follow --region ${AWS_REGION}"
echo ""
