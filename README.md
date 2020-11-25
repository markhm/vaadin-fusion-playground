# Vaadin Fusion Playground

### Issues

#### 1) Button submit on render()
I am able to submit and receive from the server without a problem. And it is nice to see that this works. But I do also see strange things. 

To get a better understanding, I am building a simple client-side component that presents the user with a multiple choice question that is loaded from the server on connectedCallback(). 

This is the question that is loaded from the server:
```
  question: {"id":1,"text":"What is the meaning of life...?","possibleAnswers":[{"id":3,"text":"42"},{"id":4,"text":"Carpe diem"},{"id":5,"text":"Memento mori"}]}
```

And this is the render() method in typescript file:
```
render() {
    const question = this.question;
    let possibleAnswers = question.possibleAnswers;

    return html`
      <h3>Welcome to today's question</h3>
      <div>The question is:</div> "${question.text}" </br>
      <div>Select your answer:</div>
      ${possibleAnswers.map(answer => html`
            <vaadin-button class="special" @click="${this.submitAnswer(answer.id)}">${answer.text}</vaadin-button> <br/>
            `)}
      <br/>
    `;
  }
```

I now see two strange things: 
1) The submitAnswer(..) method is somehow called (five times) on component initialisation...
2) and for áll the .id properties  of the question, rather than just the .id properties  from the possibleAnswers section.

I would not expect the submitAnswer(..) method to be clicked at all, unless one of the buttons are clicked and 2) I did not expect the JSON interpretation to be so sloppy that áll the .id properties are processed.
