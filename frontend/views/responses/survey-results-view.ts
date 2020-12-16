import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import {css, customElement, html, LitElement, property} from 'lit-element';
import * as SurveyResultEndpoint from "../../generated/SurveyResultEndpoint";
import QuestionResponse from "../../generated/fusion/playground/data/entity/QuestionResponse";
import SurveyResult from "../../generated/fusion/playground/data/entity/SurveyResult";

@customElement('survey-results-view')
export class ConfirmResponsesView extends LitElement {

  name: string = '';

  @property({type: String})
  surveyResultId : string = 'unavailable';

  @property({type: Object})
  surveyResult : SurveyResult | undefined;

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
    // @ts-ignore
    let score = this.surveyResult.score;

    return html`       
      <h3>Survey score</h3>
      <div>Your results were stored under: ${this.surveyResultId}</div><br/>
      
      ${this.questionResponses ? this.questionResponses.map(questionResponse =>
          html`
            ${questionResponse.questionNumber}: ${questionResponse.questionText}
            ${questionResponse.correct ? html`
                <span style='color:green'> <b>${questionResponse.responseText}</b></span> </br>
            ` :
            html`
                <span style='color:red'> <b>${questionResponse.responseText}</b></span> </br>
            `}
            </br>
          `) 
          : html`Loading your results.`}

      <br/>You scored ${score} points out of ${this.surveyResult?.survey.questions.length} </a>. <br/>
      
      <br/>See your completed surveys <a href='/completed-surveys'>here</a>.
    `;
  }

  async connectedCallback() {
    super.connectedCallback();

    this.surveyResultId = localStorage.getItem('surveyResultId') || 'unavailable';
    this.surveyResult = await SurveyResultEndpoint.get(this.surveyResultId);
    this.questionResponses = await SurveyResultEndpoint.getSurveyResponses(this.surveyResultId);
  }

}
