create table app_users(

    app_user_id bigserial primary key,
    first_name text not null,
    last_name text not null,
    email text not null,
    is_valid boolean default false,

)