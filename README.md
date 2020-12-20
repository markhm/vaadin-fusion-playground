# Vaadin Fusion Playground
A playground to build Vaadin Fusion examples and components.

A running version is found at: https://vaadin-fusion-playground.herokuapp.com/

Feel welcome to join...!

To get started:
1. Add `127.0.0.1 vaadin-fusion-playground` to `/etc/hosts`, so you can access the application on `https://vaadin-fusion-playground/`. 
1. Import the security certificate from `src/main/resources/keystore/dev/vaadin-fusion-playground.cer` to trust the https connection.
1. Start the application with `mvn spring-boot:run`.

### Features
#### Technical features
- Login via Okta integration as described [here](https://developer.okta.com/blog/2020/11/09/vaadin-spring-boot).
- Security via https, see: `./src/main/resources/keystore/instructions.txt`
- Deployment to Heroku, see `pom.xml` and `heroku-jar-deploy.sh`

#### Functional features
- Multiple choice questions and giving answers
    - Select a survey and take it. Questions and possible answers are loaded from the server
    - Response to every question is saved to the server.
- Adding achievements (work in progress)

### Not yet implemented / Feature backlog
- Creating a new user account that is stored in Okta
- Logging in via Facebook / Google
- _What would you like to add...?_

----

### Technology stack
- Vaadin 18 (latest) on OpenJDK 15 (latest)
- MongoDB (embedded)
- [Publication of Unit test results via Github Actions](https://github.com/marketplace/actions/publish-unit-test-results)

Developed on macOS (11.0.1)

---
### Known issues
- **Blocking:** How should a server-side Flow view be marked, so it is allowed according to (clients-side) security...? Currently, we see a 403.
- **Inconvenient:** Connection fails for VaadinDevmodeGizmo.js?10c7:944 WebSocket connection to 'wss://vaadin-fusion-playground:35729/' failed: Error in connection establishment: net::ERR_CONNECTION_CLOSED
- **Minor:** When surrounding the Avatar image in `main-view.ts` with a link, it is no longer outlined to the right. Why...?

### Questions 
- It seems methods in an Endpoint cannot be overloaded. Bug of Feature...?
- How can   
- _Add more questions_ 

### Answers

### What we learned / discovered
- Storing objects on the client side in the session or local storage. See [here](https://medium.com/@nixonaugustine5/localstorage-and-sessionstorage-in-angular-app-65cda19283a0).
- The @value-changed event of a <vaadin-select> box does not seem contain the new value that was selected. It can be retrieved from `event.detail.value`. NB: the (grandparent) doesn't show it is available.  
- Accessing an url parameter is explained [here](https://www.sitepoint.com/get-url-parameters-with-javascript/).
- How to access and modify elements in the DOM, e.g. a <vaadin-button>, to set it from disabled to enabled...? -> This should be done with imparative programming.
- There is no client-side API to create a user in Okta. A new user should be created with the server-side API.
- How to fix [Error: Typescript: Type 'string | undefined' is not assignable to type 'string'](https://stackoverflow.com/questions/54496398/typescript-type-string-undefined-is-not-assignable-to-type-string).

#### Reminders for building a custom component using Lit element
- Custom components need an explicit end-tag, even though they might not contain any content in between: `<custom-component-name> </custom-component-name>`.
- Only custom component of a particular type can be used per view. If two components have the same name, only the last one is shown.
- Event handlers 

### Useful resources
- [Creating clients-side forms - Binder tutorial](https://vaadin.com/docs-beta/latest/fusion/forms/tutorial-binder/)
- [Fusion Typescript examples](https://github.com/web-padawan/ts-vaadin-examples/)
- [Lit element lifecycle](https://lit-element.polymer-project.org/guide/lifecycle#firstupdated)

