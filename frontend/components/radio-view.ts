import { html, LitElement, property, customElement, PropertyValues } from 'lit-element';
import '@vaadin/vaadin-radio-button/vaadin-radio-group';
import '@vaadin/vaadin-radio-button';
import '@vaadin/vaadin-button';

// import * as AnswerEndpoint from "../generated/AnswerEndpoint";

import * as QuestionEndpoint from "../generated/QuestionEndpoint";

// import { EndpointError } from '@vaadin/flow-frontend/Connect';
// import { showNotification } from '@vaadin/flow-frontend/a-notification';
import Question from "../generated/fusion/playground/domain/Question";


@customElement('radio-view')
export class RadioView extends LitElement {

    name = "Tester";

    @property({ type: Number})
    answerId = 1;
    @property({ type: Number})
    userId = 2;
    @property({ type: Number})
    questionId = 3;

    @property({type: Array})
    answers = [
        {id: '1', name: 'True'},
        {id: '2', name: 'False'},
        {id: '3', name: 'Maybe'}
    ];

    @property({ type: Object })
    question : Question = {
        id: 0,
        text: "Not yet available",
        possibleAnswers: [{id: 1, text: "yes"}, {id: 2, text: "no"}]
    }

    // private binder = new Binder(this, AnswerModel);

    render() {
    return html`
        <div>
           <vaadin-radio-group label="Your answer" theme="vertical">
            
            ${this.answers.map(answer => html`
                <vaadin-radio-button value="${answer.id}" > ${answer.name} </vaadin-radio-button>
                `)}
            
            </vaadin-radio-group>
            <br/>
            <vaadin-button theme="primary" @click="${this.submit}">Save</vaadin-button>
        </div>
        `;
    }

    protected firstUpdated(_changedProperties: PropertyValues) {
        super.firstUpdated(_changedProperties);

        console.trace('radio-view.ts: firstUpdated() called');
    }

    async connectedCallback() {
        super.connectedCallback();

        console.trace('radio-view.ts: connectedCallback() called');

        this.question = await QuestionEndpoint.getNextQuestion();
    }

    submit(){
        // nothing
    }
}
