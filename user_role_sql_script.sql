drop SCHEMA if exists user_role;
CREATE SCHEMA if not exists user_role;
CREATE USER if not exists 'user_role'@'localhost' IDENTIFIED BY 'user_role_pass';
GRANT ALL privileges on user_role.* to 'user_role'@'localhost';

-- Table: user_management.pg_users

-- DROP TABLE user_management.pg_users;

CREATE TABLE user_role.users
(
    id integer NOT NULL,
    username varchar(50) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (`id`),
    CONSTRAINT uk_name UNIQUE (`username`)
);

------------------------------
-- Table: user_management.roles

-- DROP TABLE user_management.roles;

CREATE TABLE user_role.roles
(
    id integer NOT NULL,
    role varchar(80) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (`id`),
    CONSTRAINT uk_role UNIQUE (`role`)
);


----------------------------------------


-- Table: user_management.users_roles

-- DROP TABLE user_management.users_roles;

CREATE TABLE user_role.users_roles
(
    user_id integer NOT NULL,
    role_id integer NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (`role_id`, `user_id`),
    CONSTRAINT fk_role FOREIGN KEY (`role_id`)
        REFERENCES user_role.roles (`id`) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_user FOREIGN KEY (`user_id`)
        REFERENCES user_role.users (`id`) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

insert into user_role.users (`id`, `username`) values ('7', 'nagyanna');
insert into user_role.users (`id`, `username`) values ('2', 'levente');
insert into user_role.users (`id`, `username`) values ('3', 'alpar_pap');
insert into user_role.users (`id`, `username`) values ('4', 'renata_makkfalvi');
insert into user_role.users (`id`, `username`) values ('5', 'pistuka123');
insert into user_role.users (`id`, `username`) values ('6', 'roland_ngy');
insert into user_role.users (`id`, `username`) values ('1', 'isti_kelemen');

insert into user_role.roles (`id`, `role`) values('1', 'administrator');
insert into user_role.roles (`id`, `role`) values('2', 'user');


insert into user_role.users_roles (`user_id`, `role_id`) values ('6', '1');
insert into user_role.users_roles (`user_id`, `role_id`) values ('1', '2');
insert into user_role.users_roles (`user_id`, `role_id`) values ('2', '2');
insert into user_role.users_roles (`user_id`, `role_id`) values ('3', '2');
insert into user_role.users_roles (`user_id`, `role_id`) values ('4', '2');
insert into user_role.users_roles (`user_id`, `role_id`) values ('5', '2');
insert into user_role.users_roles (`user_id`, `role_id`) values ('7', '2');
