import { css, customElement, html, LitElement, property, internalProperty} from 'lit-element';

import { showNotification } from '@vaadin/flow-frontend/a-notification';
import { EndpointError } from '@vaadin/flow-frontend/Connect';

import '../../components/string-array-combo-box';

import * as QuestionEndpoint from "../../generated/QuestionEndpoint";
import SurveyStep from "../../generated/fusion/playground/data/entity/SurveyStep";
// import {Router} from "@vaadin/router";
// import * as SurveyEndpoint from "../../generated/SurveyEndpoint";


@customElement('edit-question-view')
export class EditQuestionView extends LitElement {

    // Does the client application have a connection to the internet at the moment...?
    @property({ type: Boolean }) online: boolean = true;
    @property( {type: Boolean }) debug = false;

    @internalProperty()
    question: SurveyStep = undefined!;

    @internalProperty()
    newPossibleAnswer: string = ''!;

    static get styles() {
        return [
            css`
        :host {
          display: block;
          padding: 1em;
        }
        .special {
          width: 300px;
        }
      `,
        ];
    }

    render() {
        // const surveyStep = this.surveyStep;
        // let possibleAnswers = surveyStep.possibleAnswers;
        // let lastQuestionAnswered = false;
        //
        // let questionTextLine;
        // let alternativeText;
        // if (surveyStep.orderNumber === -1) {
        //     lastQuestionAnswered = true;
        //     alternativeText = 'You have finished the last surveyStep.';
        // }
        // else if (surveyStep.id === "0") {
        //     alternativeText = 'Loading questions...';
        // }
        //
        // if (lastQuestionAnswered || surveyStep.id == "0") {
        //     questionTextLine = html`${alternativeText}`;
        // }
        // else {
        //     questionTextLine = html`<div>Question ${surveyStep.orderNumber} of ${this.selectedSurveyInfo.numberOfQuestions}:</div> ${surveyStep.text} </br></br>
        //        <div>Your answer:</div>`
        // }

        let questionTextLine = html`<div>Question ${this.question.questionNumber}:</div> ${this.question.text} </br>
               `

        return html`
                
      <h3>Edit question</h3>
      <!-- Show questions when they are available, else show loading warning... -->
      ${questionTextLine}

      <string-array-combo-box .items="${['multiple choice']}"
                              label="Question type"
                              value="multiple choice"
                              @value-changed="${(e : CustomEvent) => this.selectedPossibleAnswers(e)}">
      </string-array-combo-box>
      
      <br/>
      <h3>Possible answers to this question</h3>
      <div>Set the allowed multiple-choice answers to this question.</div>
      <ul>
      ${this.question.possibleAnswers.length > 0 ? html`                 
             ${this.question.possibleAnswers.map(
              (possibleAnswer) => html`
                         <li>${possibleAnswer.text}
                             <a href="delete-possibleAnswer?possibleAnswerId=${possibleAnswer.id}">delete</a>
                         </li>
                     `
      )}
         `: html`No possible answers found yet. Add your first possible answer.`}
      </ul>
      <div>Note that the allowed answers are always presented in random order.</div>
      <vaadin-text-field label="Add possible answer" 
                         value="${this.newPossibleAnswer}" 
                         @value-changed='${this.newPossibleAnswerChanged}' 
                         clear-button-visible>

      </vaadin-text-field>
      <vaadin-button @click='() => ${this.addPossibleAnswer}'>Add answer</vaadin-button>
      <br/><br/>
      <div>Questions are saved automatically.</div>
      <div>Back to <a href="edit-survey?surveyId=${this.question.surveyId}">edit survey</a>.</div>
      <br/>
      
    `;
    }

    async connectedCallback() {
        super.connectedCallback();

        // load the surveyStep
        await this.loadQuestion();
    }

    async newPossibleAnswerChanged(e: CustomEvent) {
        this.newPossibleAnswer = e.detail.value;

        console.log('changed event caught for text field: '+e.detail.value);

        await this.requestUpdate();
    }

    async addPossibleAnswer() {
        try {
            // addSurvey the possible answer
            await QuestionEndpoint.addPossibleAnswer(this.question.id, this.newPossibleAnswer);

            // clear the field
            this.newPossibleAnswer = '';

            // reload the surveyStep
            await this.loadQuestion();

        } catch (error) {
            showNotification('Some error happened. ' + error.message, {position: 'bottom-start'});

            if (error instanceof EndpointError) {
                showNotification('Server error adding new possible answer. ' + error.message, {position: 'bottom-start'});
            } else {
                throw error;
            }
        }
    }

    async selectedPossibleAnswers(e : CustomEvent){
        console.log('Caught ' + e.detail.value);
        // this.binder.value.possibleAnswers.
    }

    private async loadQuestion(){
        try {

        const urlParams = new URLSearchParams(window.location.search);
        let questionId = urlParams.get('questionId')!;

        console.log('About to load surveyStep with questionId: ' + questionId + '.');
        console.log('questionId from url: ' + questionId);

        if (questionId === 'unavailable') {
            // load from url instead
            // https://www.sitepoint.com/get-url-parameters-with-javascript/

            questionId = localStorage.getItem('questionId') || 'unavailable';
            console.log('questionId from session: ' + questionId);
        }

        this.question = await QuestionEndpoint.get(questionId) || undefined!;

        console.log('loaded surveyStep: ' + JSON.stringify(this.question));

        } catch (error) {
            if (error instanceof EndpointError) {
                showNotification('Server error loading surveyStep. ' + error.message, { position: 'bottom-start' });
            } else {
                throw error;
            }
        }
    }

    // private async saveQuestion() {
    //     try {
    //         // console.log('About to submit answer: ' + answerId + ' to surveyStep ' + this.questionId + ' for userClaims '+this.userId + '.');
    //         await QuestionEndpoint.update(this.surveyStep);
    //
    //         // showNotification('Response stored.', { position: 'bottom-start' });
    //
    //         await this.requestUpdate();
    //
    //     } catch (error) {
    //         if (error instanceof EndpointError) {
    //             showNotification('Server error. ' + error.message, { position: 'bottom-start' });
    //         } else {
    //             throw error;
    //         }
    //     }
    // }
}
