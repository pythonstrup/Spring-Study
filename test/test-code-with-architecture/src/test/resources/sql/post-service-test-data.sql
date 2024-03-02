insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values ('1', 'bell@mobidoc.us', 'bell', 'Goyang', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'ACTIVE', 0),
       ('2', 'pythonstrup@gmail.us', 'pythonstrup', 'Goyang', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab', 'PENDING', 0);

insert into `posts` (`id`, `content`, `created_at`, `modified_at`, `user_id`)
values ('1', 'hello world', 1678530673958, 1678530673958, 1);