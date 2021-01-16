import {css, customElement, html, LitElement, property, internalProperty, PropertyValues} from 'lit-element';

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

import SurveyStep from "../../generated/fusion/playground/data/entity/SurveyStep";
import {Router} from "@vaadin/router";
import SurveyInfo from "../../generated/fusion/playground/data/entity/SurveyInfo";
import * as SurveySessionEndpoint from "../../generated/SurveySessionEndpoint";

@customElement('question-view')
export class QuestionView extends LitElement {

  // Does the client application have a connection to the internet at the moment...?
  @property({ type: Boolean }) online: boolean = true;
  @property( {type: Boolean }) debug = false;

  @internalProperty()
  surveyResultId: string = '0';

  @internalProperty()
  selectedSurveyInfo: SurveyInfo = undefined!;

  @internalProperty()
  surveyStep : SurveyStep = undefined!;

  @internalProperty()
  questionId : string = "3";

  @internalProperty()
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
    const question = this.surveyStep;

    let alternativeText;

    // determine whether we are actually showing a question
    let lastQuestionAnswered = false;
    if (question.questionNumber === -1) {
      lastQuestionAnswered = true;
      alternativeText = 'You have finished the last question.';
    }
    else if (question.id === "0") {
      alternativeText = 'Loading questions...';
    }

    // render the questionTextLine with the alternative text or the actual question
    let questionTextLine;
    if (lastQuestionAnswered || question.id == "0") {
      // questionTextLine = html`${alternativeText}`;
      questionTextLine = alternativeText;
    }
    else {
      questionTextLine = html`<div>Question ${question.questionNumber} of ${this.selectedSurveyInfo.numberOfQuestions}:</div> ${question.text}</br></br>
               <div>Your answer:</div>`
    }

    return html`
      <h3>This is the '${this.selectedSurveyInfo.name}' survey</h3>
      <!-- Show questions when they are available, else show loading warning... -->
      
      <!-- SurveyStep introduction, which can be empty -->
      ${this.surveyStep.introduction}
      
      ${questionTextLine}
      
      ${question.possibleAnswers.map(possibleAnswer => html`
        <vaadin-button class="special" @click="${() => this.submitAnswer(possibleAnswer.id)}">${possibleAnswer.text}</vaadin-button> <br/>
      `)}
            
      ${lastQuestionAnswered ? html`
        <br/>
        <div>You can now review and confirm your answers. </div>
        <br/>
        <div>
            <vaadin-button @click="${() => this.confirmResponses()}">Continue</vaadin-button>
        </div>
      ` : html``}

      <br/>
      <div>The surveyResponseId is '${this.surveyResultId}'.</div> <br/>
      `;
  }

  async connectedCallback() {
    super.connectedCallback();

    // retrieve the survey session from the session and redirect if not found
    this.surveyResultId = localStorage.getItem('surveyResultId') || 'unavailable';
    this.selectedSurveyInfo = JSON.parse(localStorage.getItem('selectedSurveyInfo') || undefined!);

    // console.log('surveyResultId found: '+this.surveyResultId);

    if (this.surveyResultId === 'unavailable' || this.selectedSurveyInfo === undefined) {
      console.log('Could not find this survey, please select again.');
      Router.go('/select-survey');
    }

    await this.loadQuestion();
    // this.online = navigator.onLine;
    // window.addEventListener("online", () => (this.online = true));
    // window.addEventListener("offline", () => (this.online = false));
  }

  // private getLocation(): Object {
  //   let longitude = 0;
  //   let latitude = 0;
  //
  //   if (navigator.geolocation) {
  //     navigator.geolocation.getCurrentPosition((position)=>{
  //       longitude = position.coords.longitude;
  //       latitude = position.coords.latitude;
  //
  //       console.log('longtitude: ' + longitude);
  //       console.log('latitude: ' + latitude);
  //     });
  //   } else {
  //     console.log("No support for geolocation")
  //   }
  //
  //   return { longitude, latitude };
  // }

  protected async updated(_changedProperties: PropertyValues) {
    super.updated(_changedProperties);

    // await this.loadQuestion();

  }

  private async loadQuestion()
  {
    try {
      this.surveyStep = await SurveySessionEndpoint.getNextSurveyStep(this.surveyResultId);
    } catch (error) {
      if (error instanceof EndpointError) {
        showNotification('Server error. ' + error.message, { position: 'bottom-start' });
      } else {
        throw error;
      }
    }
  }

  private async submitAnswer(responseId: string) {
    try {
      // console.log('About to submit answer: ' + answerId + ' to surveyStep ' + this.questionId + ' for userClaims '+this.userId + '.');
      // await SurveySessionEndpoint.saveResponse(this.surveyResultId, this.surveyStep.id, responseId, JSON.stringify(this.getLocation()));
      await SurveySessionEndpoint.saveResponse(this.surveyResultId, this.surveyStep.id, responseId);

      // showNotification('Response stored.', { position: 'bottom-start' });

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

  private async confirmResponses() {
    Router.go('/confirm-responses');
  }

}
