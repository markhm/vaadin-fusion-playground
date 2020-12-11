import { css, customElement, html, LitElement } from 'lit-element';
@customElement('achievements-view')
export class AchievementsView extends LitElement {
  name: string = '';

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
        <h3>Achievements</h3>
    `;
  }
}
