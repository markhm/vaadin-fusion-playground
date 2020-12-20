import {css, customElement, html, internalProperty, LitElement, property} from 'lit-element';
import {Router} from "@vaadin/router";

import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';

import './completed-responses';

import * as SurveySessionEndpoint from "../../generated/SurveySessionEndpoint";
import QuestionResponse from "../../generated/fusion/playground/data/entity/QuestionResponse";
import SurveyInfo from "../../generated/fusion/playground/data/entity/SurveyInfo";

@customElement('confirm-responses-view')
export class ConfirmResponsesView extends LitElement {

  name: string = '';

  @property({type: String})
  surveyResultId : string = 'unavailable';

  @internalProperty()
  selectedSurveyInfo: SurveyInfo = undefined!;

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
        <h3>Confirm responses</h3>
        <completed-responses surveyResultId="${this.surveyResultId}"></completed-responses>
        
      ${this.questionResponses ? html`
        <br/>  
        <div>Do you wish to confirm these answers...? <br/></div>
        <br/>
        <vaadin-horizontal-layout class="button-layout" theme="spacing">
          <vaadin-button theme="primary" @click="${this.approve}">Confirm</vaadin-button>
          <vaadin-button @click="${this.reject}">Reject</vaadin-button>
        </vaadin-horizontal-layout>
        <br/><br/>
        
        <div>(If you made a mistake, choose 'Reject' and retake the survey.)</div>

        <br/>
        <div>Your results are logged under: ${this.surveyResultId}.</div><br/>
          
      ` : html``}
    `;
  }

  async connectedCallback() {
    super.connectedCallback();

    this.surveyResultId = localStorage.getItem('surveyResultId') || 'unavailable';
  }

  async approve() {
    await SurveySessionEndpoint.confirmResponses(this.surveyResultId);
    let surveyResult = await SurveySessionEndpoint.confirmResponses(this.surveyResultId);

    if (surveyResult.survey.gradable) {
      Router.go('/survey-results');
    }
    else {

      // destroy the surveySession, since it is complete
      localStorage.setItem('surveyResultId', 'unavailable');
      localStorage.setItem('selectedSurveyInfo' , 'unavailable');

      Router.go('/completed-surveys');
    }
  }

  async  reject() {
    await SurveySessionEndpoint.rejectResponses(this.surveyResultId);

    Router.go('/completed-surveys');
  }

}
