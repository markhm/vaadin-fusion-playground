# Vaadin Fusion Playground
A playground to build Vaadin Fusion examples.
Last updated: _Dec 11th, 2020_.

A current version of this app is found at: https://vaadin-fusion-playground.herokuapp.com/

Feel welcome to join...!

To get started:
1. Add `127.0.0.1 vaadin-fusion-playground` to `/etc/hosts`, so you can access the application on `https://vaadin-fusion-playground/`. 
1. Import the security certificate from `src/main/resources/keystore/dev/vaadin-fusion-playground.cer` to trust the https connection.
1. Start the application with `mvn spring-boot:run`.

### Current features
#### Technical features
- Login via Okta integration as described [here](https://developer.okta.com/blog/2020/11/09/vaadin-spring-boot).
- Security via https, see: `./src/main/resources/keystore/`
- Deployment to Heroku, see `pom.xml` and `heroku-jar-deploy.sh`

#### Functional features
- Getting questions and giving answers
    - Questions and possible answers are loaded from the server
    - Results are saved to the server
- Adding achievements (work in progress)

### Not yet implemented / Feature backlog
- Creating a new user account
- Logging in via Facebook / Google
- _What would you like to add...?_

----

### Technology stack
- OpenJDK 15 (latest)
- Vaadin 18 (latest)
- MongoDB (embedded !) 

Developed on macOS (11.0.1)
