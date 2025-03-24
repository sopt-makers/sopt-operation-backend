CREATE TABLE if not exists banners (
     id bigserial not null,
     created_date timestamp(6),
     last_modified_date timestamp(6),
     content_type varchar(255) not null,
    mobileImageUrl varchar(255) not null,
    pcImageUrl varchar(255) not null,
     link varchar(255),
     location varchar(255) not null,
     end_date date not null,
     start_date date not null,
     publisher varchar(255) not null,
     primary key (id)
);

DELETE FROM banners;

INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2031-06-10', '2031-06-17','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2031-06-01', '2031-06-30','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2031-01-01', '2031-06-30','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2030-01-01', '2030-12-31','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2030-01-01', '2030-06-30','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2030-01-01', '2030-12-31','user');

INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2024-12-01', '2024-12-31','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2024-12-01', '2025-12-01','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2024-01-01', '2025-06-30','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2024-06-30', '2025-06-30','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2024-06-30', '2025-01-01','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2024-01-01', '2025-01-01','user');

INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2011-06-10', '2011-06-17','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2011-06-01', '2011-06-30','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2011-01-01', '2011-06-30','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2010-01-01', '2011-12-31','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2010-01-01', '2010-06-30','user');
INSERT INTO banners (content_type, mobileImageUrl, pcImageUrl, location, start_date, end_date, publisher)
VALUES ('PRODUCT', '','','PLAYGROUND_COMMUNITY','2010-01-01', '2010-12-31','user');


