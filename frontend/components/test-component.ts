import { html, LitElement, customElement } from 'lit-element';

@customElement('test-component')
export class TestComponent extends LitElement {

    name = "Mark";

    render() {
        return html`
           The name is: "${this.name}".
    `;
    }
}
