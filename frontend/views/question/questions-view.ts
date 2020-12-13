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
import * as SurveyEndpoint from "../../generated/SurveyEndpoint";
import * as ResponseEndpoint from "../../generated/ResponseEndpoint";
import Question from "../../generated/fusion/playground/data/entity/Question";

@customElement('questions-view')
export class QuestionsView extends LitElement {

  // Does the client application have a connection to the internet at the moment...?
  @property({ type: Boolean }) online: boolean = true;

  @property( {type: Boolean }) debug = false;

  // Name of the current surveyName that is being taken
  @property({ type: String }) surveyName: string = 'example';

  @property({ type: String }) surveyResponseId: string = '0';

  @property({ type: Object })
  question : Question = {
    id: "0",
    orderNumber: 0,
    surveyName: 'example',
    text: "Loading questions...",
    possibleAnswers: []
  };

  @property({ type: String})
  answerId = "0";
  @property({ type: String})
  userId = "1";
  @property({ type: String})
  questionId = "3";

  @property({ type: Number})
  totalNumberOfQuestions = 0;

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
    let alreadyAnswered = false;

    let result;
    let responseBasis;
    if (question.orderNumber === -1)
    {
      alreadyAnswered = true;
      responseBasis = 'You have completed this survey.';
    }
    else if (question.id === "0")
    {
      responseBasis = 'Loading questions...';
    }
    else{

    }

    if (alreadyAnswered || question.id == "0") {
      result = html`${responseBasis}`;
    }
    else
    {
      result = html`<div>Question ${question.orderNumber} of ${this.totalNumberOfQuestions}:</div> "${question.text}" </br></br>
               <div>Your answer:</div>`
    }

    return html`
      <h3>Welcome to this survey</h3>
      <div>The surveyResponseId is '${this.surveyResponseId}'.</div> <br/>
      <!-- Show questions when they are available, else show loading warning... -->
      ${result}
      ${possibleAnswers.map(possibleAnswer => html`
            <vaadin-button class="special" @click="${() => this.submitAnswer(possibleAnswer.id)}">${possibleAnswer.text}</vaadin-button> <br/>
            `)}
      <br/>
      ${alreadyAnswered ? html`<br/>See your responses <a href="/responses?surveyResponseId=${this.surveyResponseId}">here</a>.` : html``}
    `;
  }

  async connectedCallback() {
    super.connectedCallback();

    // get the url parameter 'surveyName' to load the proper surveyName

    const queryString = window.location.search;
    console.log('queryString: ' + queryString);
    const urlParams = new URLSearchParams(queryString);
    this.surveyName = urlParams.get('surveyName') || 'example';
    console.log('surveyName selected: ' + this.surveyName);

    this.online = navigator.onLine;
    window.addEventListener("online", () => (this.online = true));
    window.addEventListener("offline", () => (this.online = false));

    if (this.surveyResponseId == "0")
    {
      this.surveyResponseId = await ResponseEndpoint.beginSurvey(this.surveyName, this.userId);
    }

    await this.loadQuestion();
    this.totalNumberOfQuestions = await SurveyEndpoint.getTotalNumberOfQuestions(this.surveyName);
  }

  // @ts-ignore
  private async submit() {
    try {

      await ResponseEndpoint.saveResponse(this.surveyResponseId, this.questionId, this.userId, this.answerId);

      showNotification('Answer details stored.', { position: 'bottom-start' });
      await this.loadQuestion();

    } catch (error) {
      if (error instanceof EndpointError) {
        showNotification('Server error. ' + error.message, { position: 'bottom-start' });
      } else {
        throw error;
      }
    }
  }

  private async submitAnswer(answerId: string) {

    // console.log('About to submit answer: ' + answerId + ' to question ' + this.questionId + ' for user '+this.userId + '.');
    await ResponseEndpoint.saveResponse(this.surveyResponseId, this.question.id, this.userId, answerId);

    // load next question from the server
    await this.loadQuestion();
    await this.requestUpdate();
  }

  private async loadQuestion()
  {
    this.question = await SurveyEndpoint.getNextQuestion(this.surveyResponseId);
    // console.log('Question loaded is: ' + JSON.stringify(this.question));
  }


}

/// <vaadin-button theme="primary" @click="${this.submit}">Submit</vaadin-button>

// <br/>
// <hr/>
//
// ${!this.online ? html`<div>You need to be online to submit an answer</div>` : `<div>Congratulations, you are online</div>`}
