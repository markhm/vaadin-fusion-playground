import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import {css, customElement, html, internalProperty, LitElement, property} from 'lit-element';
import * as SurveySessionEndpoint from "../../generated/SurveySessionEndpoint";
import QuestionResponse from "../../generated/fusion/playground/data/entity/QuestionResponse";
import {Router} from "@vaadin/router";
import SurveyResult from "../../generated/fusion/playground/data/entity/SurveyResult";
import SurveyInfo from "../../generated/fusion/playground/data/entity/SurveyInfo";

@customElement('confirm-responses-view')
export class ConfirmResponsesView extends LitElement {

  name: string = '';

  @property({type: String})
  surveyResultId : string = 'unavailable';

  @property({type: Object})
  surveyResult : SurveyResult | undefined;

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
      <h3>Your responses</h3>
      <div>Survey: "${this.selectedSurveyInfo.name} (${this.selectedSurveyInfo.category})"</div>
      
      ${this.questionResponses ? this.questionResponses.map(questionResponse => html`
            ${questionResponse.questionNumber}: ${questionResponse.questionText} <b>${questionResponse.responseText}</b> </br>
      `) : html`Loading your answers.`}
      
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
    this.selectedSurveyInfo = JSON.parse(localStorage.getItem('selectedSurveyInfo') || undefined!);
    this.questionResponses = await SurveySessionEndpoint.getSurveyResponses(this.surveyResultId);
  }

  async approve() {
    await SurveySessionEndpoint.confirmResponses(this.surveyResultId);
    this.surveyResult = await SurveySessionEndpoint.confirmResponses(this.surveyResultId);

    if (this.surveyResult.survey.gradable) {
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
    this.surveyResult = await SurveySessionEndpoint.rejectResponses(this.surveyResultId);
    console.log('surveyResult: '+this.surveyResult);

    Router.go('/completed-surveys');
  }

}
