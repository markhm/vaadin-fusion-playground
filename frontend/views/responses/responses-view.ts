import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import {css, customElement, html, LitElement, property} from 'lit-element';
import * as ResponseEndpoint from "../../generated/ResponseEndpoint";
import QuestionResponse from "../../generated/fusion/playground/data/entity/QuestionResponse";

@customElement('responses-view')
export class ResponsesView extends LitElement {
  name: string = '';

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
      <vaadin-button @click="${this.loadAnswers}">Show responses</vaadin-button> <br/><br/>
      ${this.questionResponses.map(questionResponse => html`
            ${questionResponse.questionNumber}: ${questionResponse.questionText} <b>${questionResponse.responseText}</b> </br>
      `)}
    `;
  }

  async loadAnswers() {
    this.questionResponses = await ResponseEndpoint.getCategoryAnswers('1', 'example');

    await this.requestUpdate();
  }
}
