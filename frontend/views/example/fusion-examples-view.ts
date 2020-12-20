import { css, customElement, html, LitElement } from 'lit-element';

@customElement('fusion-examples-view')
export class FusionExamplesView extends LitElement {
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
            <a href='test/hello-world'>Hello World</a><br/>
            <a href='people'>People view</a><br/>
            <a href='events'>Events</a><br/>
            
            
        `;
    }

}
