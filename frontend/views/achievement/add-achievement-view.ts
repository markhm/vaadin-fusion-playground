import { showNotification } from '@vaadin/flow-frontend/a-notification';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-select'
import '@vaadin/vaadin-date-picker';

import { css, customElement, html, property, LitElement } from 'lit-element';

@customElement('add-achievement-view')
export class AddAchievementView extends LitElement {

    name: string = 'Test';

    date: Date = new Date();

    @property ({type: Array})
    items: String[] = [ '5,000 steps', '10,000 steps', '15,000 steps', '20,000 steps'];

    @property({  type: Object } )
    departments : object = [
        {value: '1', label: 'Product'},
        {value: '2', label: 'Service'},
        {value: '3', label: 'HR'},
        {value: '4', label: 'Accounting'}
    ];

    static get styles() {
        return css`
      :host {
        display: block;
        padding: 1em;
      }
    `;
    }

    render() {

        let data = this.items;

        return html`
            <vaadin-date-picker 
                    readonly
                    label="Date" 
                    value="${this.date.toISOString().split('T')[0]}">
            </vaadin-date-picker>
            
            <br/>

            <vaadin-select id="select" label="Achievement type" @value-changed="${this.somethingChanged}" placeholder="Select # steps">
                <template>
                    <vaadin-list-box>
                        ${data.map(dataElement => html`
                            <vaadin-item>${dataElement}</vaadin-item>
                        `)}
                    </vaadin-list-box>
                </template>
            </vaadin-select>
            
            <br/><br/>
            <vaadin-button @click="${this.submit}">Submit</vaadin-button>
    `;
    }
    // value="${this.items[0]}"

    // items="${this.departments}"

// <template>
//     [[index]]: [[item.label]] <b>[[item.value]</b>
//     </template>

// <vaadin-combo-box
//     label="Steps achieved"
//     items="[[${data}]]"
//     value='One'
// @value-changed="${this.valueChanged}">
//         </vaadin-combo-box>

    // valueChanged(e: CustomEvent) {
    //     console.log('valueChanged: CustomEvent: ' + JSON.stringify(e));
    //     // this.id = e.detail.id;
    // }

    somethingChanged(e: CustomEvent) {
        console.log('valueChanged: CustomEvent: ' + JSON.stringify(e));
        // this.id = e.detail.id;
    }

    submit() {
        console.log('departments:' + JSON.stringify(this.departments));

        showNotification('Hello ' + this.name);
    }


}

