import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import {css, customElement, html, LitElement, property} from 'lit-element';
import * as ResponseEndpoint from "../../generated/ResponseEndpoint";
import QuestionResponse from "../../generated/fusion/playground/data/entity/QuestionResponse";
import {Router} from "@vaadin/router";

@customElement('confirm-responses-view')
export class ConfirmResponsesView extends LitElement {

  name: string = '';

  @property({type: String})
  surveyResultId : string = 'unavailable';

  // @property({type: Object})
  // surveyResult : SurveyResult;

  @property({ type: Array })
  questionResponses : QuestionResponse[] = [];

  static get styles() {
    return css`
      :host {
        display: block;
        padding: 1em;
      }
    `;
  }

  render() {
    return html`       
      <h3>Survey responses</h3>
      <div>You took survey: ${this.surveyResultId}</div><br/>
      
      ${this.questionResponses ? this.questionResponses.map(questionResponse => html`
            ${questionResponse.questionNumber}: ${questionResponse.questionText} <b>${questionResponse.responseText}</b> </br>
      `) : html`Loading your answers.`}
      
      ${this.questionResponses ? html`
        <br/>  
        <div>Do you wish to submit these answers...?</div>
        <br/>
        <vaadin-horizontal-layout class="button-layout" theme="spacing">
          <vaadin-button theme="primary" @click="${this.approve}">Approve</vaadin-button>
          <vaadin-button @click="${this.reject}">Reject</vaadin-button>
        </vaadin-horizontal-layout>

      ` : html``}
    `;
  }

  async connectedCallback() {
    super.connectedCallback();

    this.surveyResultId = localStorage.getItem('surveyResultId') || 'unavailable';

    this.questionResponses = await ResponseEndpoint.getSurveyResponses(this.surveyResultId);
  }

  async approve() {

    await ResponseEndpoint.approveResponses(this.surveyResultId);

    Router.go('/completed-surveys');
  }

  async  reject() {
    await ResponseEndpoint.rejectResponses(this.surveyResultId);

    Router.go('/completed-surveys');
  }

}
