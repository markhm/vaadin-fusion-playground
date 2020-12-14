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

Developed on macOS (11.0.1)

---
### Known issues
- **Blocking:** How should a server-side Flow view be marked, so it is allowed according to (clients-side) security...? Currently, we see a 403.
- **Blocking:** On the surveys-view page, the list of available surveys is correctly retrieved from the endpoint, but not processed correctly (Error. The client-side default ones had to be set.
- **Inconvenient:** Connection fails for VaadinDevmodeGizmo.js?10c7:944 WebSocket connection to 'wss://vaadin-fusion-playground:35729/' failed: Error in connection establishment: net::ERR_CONNECTION_CLOSED
- **Minor:** When surrounding the Avatar image in `main-view.ts` with a link, it is no longer outlined to the right. Why...?

### Questions 
- How to access and modify elements in the DOM, e.g. a <vaadin-button>, to set it from disabled to enabled...?
  How to store or retrieve an objects on the client side in the session or local storage...?
  
### What we learned / discovered
- It seems methods in an Endpoint cannot be overloaded. Bug of Feature...?
- Accessing an url parameter, see [here](https://www.sitepoint.com/get-url-parameters-with-javascript/).  
- ~~The @value-changed event of a <vaadin-select> box does nót contain the new value that was selected. The event looks like this: valueChanged: CustomEvent {"isTrusted":false}. How can we access/use the new value...?~~ - It turns out the `event.detail.value` contains the value, even though the (grandparent) doesn't.
- There is no client-side API to create a user in Okta. A new user should be created with the server-side API.
