--DROP TABLE IF EXISTS last_access;

CREATE TABLE IF NOT EXISTS last_access (
    `row_id` integer primary key auto_increment,
    `last_num` bigint
);

CREATE TABLE IF NOT EXISTS rejections (
                 `sent_date` bigint primary key auto_increment,
                 `rej_id` bigint ,
                 `name` varchar(250),
                 `address` varchar(250),
                 `cure_name` varchar(250),
                 `dose` varchar(100),
                 `form` varchar(100),
                 `pack` varchar(100),
                 `ordered_amount` int,
                 `received_amount` int,
                 `r_Cause` varchar(250),
                 `is_sent` bit
);