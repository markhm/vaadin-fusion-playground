import {css, customElement, html, LitElement, property} from 'lit-element';

import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';

import './components/completed-responses';

import * as SurveySessionEndpoint from "../../generated/SurveySessionEndpoint";
import SurveyResult from "../../generated/fusion/playground/data/entity/SurveyResult";

@customElement('survey-result-view')
export class SurveyResultView extends LitElement {

  name: string = '';

  @property({type: String})
  surveyResultId : string = 'unavailable';

  @property({type: Object})
  surveyResult : SurveyResult | undefined;

  static get styles() {
    return css`
      :host {
        display: block;
        padding: 1em;
      }
    `;
  }

  render() {
    // @ts-ignore
    let score = this.surveyResult.score;

    return html`       
      <h3>Survey score</h3>
      <completed-responses surveyResultId="${this.surveyResultId}"> </completed-responses>

      <br/>
      <div>You scored ${score} points out of ${this.surveyResult?.survey.surveySteps.length}.</div>
      <br/>
      <div>See your completed surveys <a href='/completed-surveys'>here</a>.</div>
      <br/>
      <div>Your results were stored under: ${this.surveyResultId}.</div><br/>
    `;
  }

  async connectedCallback() {
    super.connectedCallback();

    this.surveyResultId = localStorage.getItem('surveyResultId') || 'unavailable';
    this.surveyResult = await SurveySessionEndpoint.get(this.surveyResultId);
  }


}
