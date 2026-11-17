# GitHub upload checklist

Before making this repository public:

- [ ] Run `mvnw.cmd test` inside each of the three service folders.
- [ ] Start the services against your own local MySQL or with Docker Compose.
- [ ] Test customer creation, duplicate CIN/email, update and delete.
- [ ] Test account creation, deposit, withdrawal, transfer and insufficient balance.
- [ ] Test Transaction Service with Account Service running.
- [ ] Confirm Swagger UI opens for all three services.
- [ ] Confirm `.env`, real passwords, tokens and private files are not committed.
- [ ] Add screenshots of Swagger/Postman and a successful Docker Compose run to the README.
- [ ] Commit the source code, not `target/` build folders.

The ZIP was generated from the three uploaded project archives and has not been executed against your own MySQL/Windows environment. Do not describe the project as fully tested until you complete these checks.
