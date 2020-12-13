import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import {css, customElement, html, LitElement, property} from 'lit-element';
import * as ResponseEndpoint from "../../generated/ResponseEndpoint";
import QuestionResponse from "../../generated/fusion/playground/data/entity/QuestionResponse";

@customElement('responses-view')
export class ResponsesView extends LitElement {
  name: string = '';

  @property({type: String}) surveyResponseId = '';

  @property({ type: Array }) questionResponses : QuestionResponse[] = [];

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
      <div>You took survey: ${this.surveyResponseId}</div><br/>
      <vaadin-button @click="${this.loadAnswers}">Show your responses</vaadin-button> <br/><br/>
      ${this.questionResponses.map(questionResponse => html`
            ${questionResponse.questionNumber}: ${questionResponse.questionText} <b>${questionResponse.responseText}</b> </br>
      `)}
    `;
  }

  async connectedCallback() {
    super.connectedCallback();

    const queryString = window.location.search;
    console.log('queryString: ' + queryString);
    const urlParams = new URLSearchParams(queryString);
    this.surveyResponseId = urlParams.get('surveyResponseId') || '0';
    console.log('surveyResponseId selected: ' + this.surveyResponseId);
  }


  async loadAnswers() {
    this.questionResponses = await ResponseEndpoint.getSurveyAnswers('1', 'example');

    await this.requestUpdate();
  }
}
