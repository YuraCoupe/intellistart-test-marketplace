To connect to Heroku PostgreSQL DB you should use next environmental variables
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.url=${DB_URL}

Default admin account is added to DB from the start.
Set up its credentials with the next environmental variables:
DEFAULT_ADMIN_NAME
DEFAULT_ADMIN_PASSWORD