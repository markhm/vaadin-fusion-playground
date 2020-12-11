import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import {css, customElement, html, LitElement, property} from 'lit-element';
import Answer from "../../generated/fusion/playground/data/entity/Answer";
import * as AnswerEndpoint from "../../generated/AnswerEndpoint";

@customElement('answers-view')
export class AnswersView extends LitElement {
  name: string = '';

  @property({ type: Array })
  answers : Answer[] = [];

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
      ${this.answers.map(answer => html`
            ${answer.id}: ${answer.answer}
      `)}
    `;
  }

  async loadAnswers() {
    this.answers = await AnswerEndpoint.getAnswers('1', 'example');

    await this.requestUpdate();
  }
}
