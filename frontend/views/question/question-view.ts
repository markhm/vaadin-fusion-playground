import { css, customElement, html, LitElement, property} from 'lit-element';

import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-grid/all-imports.js';
import '@vaadin/vaadin-icons/vaadin-icons.js';
import '@vaadin/vaadin-lumo-styles/all-imports.js';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-ordered-layout/vaadin-vertical-layout.js';
import '@vaadin/vaadin-radio-button/vaadin-radio-group';
import '@vaadin/vaadin-radio-button';

import { showNotification } from '@vaadin/flow-frontend/a-notification';
import { EndpointError } from '@vaadin/flow-frontend/Connect';

import * as SurveyEndpoint from "../../generated/SurveyEndpoint";
import * as SurveySessionEndpoint from "../../generated/SurveySessionEndpoint";
import Question from "../../generated/fusion/playground/data/entity/Question";
import {Router} from "@vaadin/router";

@customElement('question-view')
export class QuestionView extends LitElement {

  // Does the client application have a connection to the internet at the moment...?
  @property({ type: Boolean }) online: boolean = true;
  @property( {type: Boolean }) debug = false;

  @property({ type: String }) surveyResultId: string = '0';

  @property({ type: Object })
  question : Question = {
    id: "0",
    orderNumber: 0,
    text: "Loading questions...",
    possibleAnswers: []
  };

  @property({ type: String}) questionId : string = "3";
  @property({ type: Number}) totalNumberOfQuestions = 0;

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
    if (question.orderNumber === -1) {
      alreadyAnswered = true;
      responseBasis = 'You have finished the last question.';
    } else if (question.orderNumber === -2)
    {

    }
    else if (question.id === "0") {
      responseBasis = 'Loading questions...';
    }
    else{
    }

    if (alreadyAnswered || question.id == "0") {
      result = html`${responseBasis}`;
    }
    else {
      result = html`<div>Question ${question.orderNumber} of ${this.totalNumberOfQuestions}:</div> "${question.text}" </br></br>
               <div>Your answer:</div>`
    }

    return html`
      <h3>Welcome to this survey</h3>
      <!-- Show questions when they are available, else show loading warning... -->
      ${result}
      ${possibleAnswers.map(possibleAnswer => html`
            <vaadin-button class="special" @click="${() => this.submitAnswer(possibleAnswer.id)}">${possibleAnswer.text}</vaadin-button> <br/>
            `)}
      <br/>
      ${alreadyAnswered ? html`<br/>Please confirm your responses <a href='/confirm-responses'>here</a>.` : html``}

      <div>The surveyResponseId is '${this.surveyResultId}'.</div> <br/>
    `;
  }

  async connectedCallback() {
    super.connectedCallback();

    // retrieve the survey session from the session and redirect if not found
    this.surveyResultId = localStorage.getItem('surveyResultId') || 'unavailable';

    console.log('surveyResultId found: '+this.surveyResultId);

    if (this.surveyResultId === 'unavailable') {
      Router.go('/select-survey');
    }

    // this.online = navigator.onLine;
    // window.addEventListener("online", () => (this.online = true));
    // window.addEventListener("offline", () => (this.online = false));

    this.getTotalNumberOfQuestions();

    await this.loadQuestion();
  }

  private async submitAnswer(responseId: string) {
    try {
      // console.log('About to submit answer: ' + answerId + ' to question ' + this.questionId + ' for user '+this.userId + '.');
      await SurveySessionEndpoint.saveResponse(this.surveyResultId, this.question.id, responseId);

      showNotification('Response stored.', { position: 'bottom-start' });

      await this.loadQuestion();

      await this.requestUpdate();

    } catch (error) {
      if (error instanceof EndpointError) {
        showNotification('Server error. ' + error.message, { position: 'bottom-start' });
      } else {
        throw error;
      }
    }
  }

  private async loadQuestion()
  {
    try {
      this.question = await SurveySessionEndpoint.getNextQuestion(this.surveyResultId);
    } catch (error) {
      if (error instanceof EndpointError) {
        showNotification('Server error. ' + error.message, { position: 'bottom-start' });
      } else {
        throw error;
      }
    }
  }

  private async getTotalNumberOfQuestions()
  {
    try {
      this.totalNumberOfQuestions = await SurveyEndpoint.getTotalNumberOfQuestions(this.surveyResultId);
    } catch (error) {
      if (error instanceof EndpointError) {
        showNotification('Server error. ' + error.message, { position: 'bottom-start' });
      } else {
        throw error;
      }
    }
  }

}
