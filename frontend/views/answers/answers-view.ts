import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import {css, customElement, html, LitElement, property} from 'lit-element';
import * as AnswerEndpoint from "../../generated/AnswerEndpoint";
import AnsweredQuestion from "../../generated/fusion/playground/data/entity/AnsweredQuestion";

@customElement('answers-view')
export class AnswersView extends LitElement {
  name: string = '';

  @property({ type: Array })
  answeredQuestions : AnsweredQuestion[] = [];

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
      <vaadin-button @click="${this.loadAnswers}">Show answers</vaadin-button> <br/>
      ${this.answeredQuestions.map(answeredQuestion => html`
            ${answeredQuestion.questionNumber}: ${answeredQuestion.questionText} <b>${answeredQuestion.answerText}</b> </br>
      `)}
    `;
  }

  async loadAnswers() {
    this.answeredQuestions = await AnswerEndpoint.getCategoryAnswers('1', 'example');

    await this.requestUpdate();
  }
}
