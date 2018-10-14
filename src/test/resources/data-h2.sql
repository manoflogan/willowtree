insert into user_profile(id, first_name, last_name, job_title, slug, profile_type, profile_id) values(1, 'Jane', 'Doe', 'Vice President', 'jane-doe', 'people', 'profile_1');
insert into user_profile(id, first_name, last_name, job_title, slug, profile_type, profile_id) values(2, 'John', 'Doe', 'Principle Software Engineer', 'john-doe', 'people', 'profile_2');
insert into user_profile(id, first_name, last_name, job_title, slug, profile_type, profile_id) values(3, 'Mary', 'Lamb', 'Software Engineer', 'mary-lamb', 'people', 'profile_3');
insert into user_profile(id, first_name, last_name, job_title, slug, profile_type, profile_id) values(4, 'Jack', 'Smith', 'Chief Talent Officer', 'jack-smith', 'people', 'profile_4');
insert into user_profile(id, first_name, last_name, job_title, slug, profile_type, profile_id) values(5, 'Jill', 'Smith', 'Senior Technical Recruiter', 'jill-smith', 'people', 'profile_5');
insert into user_profile(id, first_name, last_name, job_title, slug, profile_type, profile_id) values(6, 'Mary', 'Smith', 'Senior Software Engineer', 'mary-smith', 'people', 'profile_6');

insert into head_shot(profile_id, headshot_id, alt, url, headshot_type, mime_type, height, width) values(1, 'headshot_id_1', 'alt Jane Doe', '//images.ctfassets.net/headshot_jane_doe.jpg', 'image/jpeg', 340, 340);
insert into head_shot(profile_id, headshot_id, alt, url, headshot_type, mime_type, height, width) values(1, 'headshot_id_2', 'alt John Doe', '//images.ctfassets.net/headshot_john_doe.jpg', 'image/jpeg', 340, 340);
insert into head_shot(profile_id, headshot_id, alt, url, headshot_type, mime_type, height, width) values(1, 'headshot_id_3', 'alt Mary Lamb', '//images.ctfassets.net/headshot_mary_lamb.jpg', 'image/jpeg', 340, 340);
insert into head_shot(profile_id, headshot_id, alt, url, headshot_type, mime_type, height, width) values(1, 'headshot_id_4', 'alt Jack Smith', '//images.ctfassets.net/headshot_jack_smith.jpg', 'image/jpeg', 340, 340);
insert into head_shot(profile_id, headshot_id, alt, url, headshot_type, mime_type, height, width) values(1, 'headshot_id_5', 'alt Jill Smith', '//images.ctfassets.net/headshot_jill_smith.jpg', 'image/jpeg', 340, 340);
insert into head_shot(profile_id, headshot_id, alt, url, headshot_type, mime_type, height, width) values(1, 'headshot_id_6', 'alt Mary Smith', '//images.ctfassets.net/headshot_mary_smith.jpg', 'image/jpeg', 340, 340);

insert into social_links(social_link_id, call_to_action, social_link_type, url, profile_id) values(1, 'Follow Jane Doe on LinkedIn', 'linkedin', 'https://www.linkedin.com/in/jillsmith7/', 5);