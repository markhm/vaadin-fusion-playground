import { css, customElement, html, LitElement, property } from 'lit-element';

import { showNotification } from '@vaadin/flow-frontend/a-notification';

import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-grid/all-imports.js';
import '@vaadin/vaadin-icons/vaadin-icons.js';
import '@vaadin/vaadin-lumo-styles/all-imports.js';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-ordered-layout/vaadin-vertical-layout.js';

import '@vaadin/vaadin-radio-button/vaadin-radio-group';
import '@vaadin/vaadin-radio-button';

import { EndpointError } from '@vaadin/flow-frontend/Connect';
import * as QuestionEndpoint from "../../generated/QuestionEndpoint";
import * as AnswerEndpoint from "../../generated/AnswerEndpoint";
import Question from "../../generated/fusion/playground/domain/Question";


@customElement('question-view')
export class QuestionView extends LitElement {

  @property({ type: Boolean })
  online: boolean = true;

  @property({ type: Object })
  question : Question = {
    id: 0,
    text: "This is a client-side question",
    possibleAnswers: [{id: 1, text: "yes"}, {id: 2, text: "no"}]
  };

  @property({ type: Number})
  answerId = 1;
  @property({ type: Number})
  userId = 2;
  @property({ type: Number})
  questionId = 3;


  @property({type: Array}) // local answers
  answers = [
    {id: '1', name: 'True'},
    {id: '2', name: 'False'},
    {id: '3', name: 'Maybe'}
  ];

  static get styles() {
    return [
      css`
        :host {
          display: block;
          padding: 1em;
        }
        .special {
          width: 300px;
        }
      `,
    ];
  }

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

  // ${field(this.binder.model.answer)}

  // initialization
  async connectedCallback() {
    super.connectedCallback();

    console.trace('radio-view.ts: firstUpdated() called');

    this.online = navigator.onLine;
    window.addEventListener("online", () => (this.online = true));
    window.addEventListener("offline", () => (this.online = false));

    this.question = await QuestionEndpoint.getNextQuestion();

    console.log('received questions from server: '+JSON.stringify(this.question));
  }

  // @ts-ignore
  private async submit() {
    try {

      const answer = await AnswerEndpoint.addAnswer(this.questionId, this.userId, this.answerId);

      console.log('@submit: Received new answer from server side: ' + JSON.stringify(answer));

      showNotification('Answer details stored.', { position: 'bottom-start' });

    } catch (error) {
      if (error instanceof EndpointError) {
        showNotification('Server error. ' + error.message, { position: 'bottom-start' });
      } else {
        throw error;
      }
    }
  }

  private async submitAnswer(answerId: number) {
    console.log('About to submit answer: ' + answerId);

    const answer = await AnswerEndpoint.addAnswer(this.questionId, this.userId, answerId);

    console.log('@submitAnswer: Received new answer from server side: ' + JSON.stringify(answer));
  }

}


// this.questions = [
//   {
//     text: 'Is there really life beyond a client-side question...?',
//     possibleAnswers: [{id: 1, text: "yes"}, {id: 2, text: "no"}]
//   },
//   {
//     text: 'Is there meaning in having another client-side question...?',
//     possibleAnswers: [{id: 1, text: "maybe"}, {id: 2, text: "true"}, {id: 3, text: "false"}]
//   },
// ];


/// <vaadin-button theme="primary" @click="${this.submit}">Submit</vaadin-button>

// <br/>
// <hr/>
//
// ${!this.online ? html`<div>You need to be online to submit an answer</div>` : `<div>Congratulations, you are online</div>`}
