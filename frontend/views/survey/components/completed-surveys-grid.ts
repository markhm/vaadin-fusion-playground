import { LitElement, property, html, query} from 'lit-element';

import { render } from 'lit-html';
import '@vaadin/vaadin-grid/vaadin-grid.js';
import '@vaadin/vaadin-checkbox/vaadin-checkbox.js';

import '../../responses/completed-responses';

import type { CheckboxElement } from '@vaadin/vaadin-checkbox';
import type { GridElement, GridItem, GridItemModel } from '@vaadin/vaadin-grid';
// import type { GridElement, GridItemModel } from '@vaadin/vaadin-grid';
import type { GridColumnElement } from '@vaadin/vaadin-grid/vaadin-grid-column.js';

import SurveyResult from "../../../generated/fusion/playground/data/entity/SurveyResult";

// import SurveyResult from "../../../generated/fusion/playground/data/entity/SurveyResult";

const itemCache = new WeakMap<HTMLElement>();

class CompletedSurveysGrid extends LitElement {
    @property({ type: Array })
    surveyResults = [];

    @query('vaadin-grid')
    private grid!: GridElement;

    private _boundToggleDetailsRenderer = this._toggleDetailsRenderer.bind(this);
    private _boundRowDetailsRenderer = this._rowDetailsRenderer.bind(this);

    //
    render() {
        return html`
      <vaadin-grid .items="${this.surveyResults}" .rowDetailsRenderer="${this._boundRowDetailsRenderer}">
          <vaadin-grid-column header='Survey name (category)' .renderer="${this._surveyNameCategoryRenderer}"></vaadin-grid-column>
          <vaadin-grid-column header='Finished on' .renderer="${this._finishedOnRenderer}"></vaadin-grid-column>
          <vaadin-grid-column header='Last column' .renderer="${this._boundToggleDetailsRenderer}"></vaadin-grid-column>
      </vaadin-grid>
    `;
    }

    connectedCallback() {
        super.connectedCallback();

        console.log('Found surveyResults: '+JSON.stringify(this.surveyResults));
    }

    // get endpoint() {
    //     return 'https://demo.vaadin.com/demo-data/1.0';
    // }
    //
    // firstUpdated() {
    //     fetch(`${this.endpoint}/people?count=200`)
    //         .then((r) => r.json())
    //         .then((data) => {
    //             this.users = data.result;
    //         });
    // }

    _onCheckboxChange(e: CustomEvent) {
        const checkbox = e.target as HTMLElement;
        this._toggleDetails(e.detail.value, itemCache.get(checkbox.parentNode as HTMLElement));
    }

    _toggleDetails(value: boolean, item: GridItem) {
        if (value) {
            this.grid.openItemDetails(item);
        } else {
            this.grid.closeItemDetails(item);
        }
    }

    _toggleDetailsRenderer(root: HTMLElement, _column: GridColumnElement, model: GridItemModel) {
        // only render the checkbox once, to avoid re-creating during subsequent calls
        if (!root.firstElementChild) {
            render(
                html`
          <vaadin-checkbox @checked-changed="${this._onCheckboxChange}">
            Show description
          </vaadin-checkbox>
        `,
                root,
                { eventContext: this } // bind event listener properly
            );
        }
        const { item } = model;
        // store the item to avoid grid virtual scrolling reusing DOM nodes to mess it up
        itemCache.set(root, item);
        const detailsOpened = this.grid.detailsOpenedItems || [];
        (root.firstElementChild as CheckboxElement).checked = detailsOpened.indexOf(item) > -1;
    }

    _surveyNameCategoryRenderer(root: HTMLElement, _grid: GridElement, model: GridItemModel) {
        const surveyResult = model.item as SurveyResult;
        render(html`${surveyResult.survey.name} (${surveyResult.survey.category})`, root);
    }

    _surveyResultIdRenderer(root: HTMLElement, _grid: GridElement, model: GridItemModel) {
        const surveyResult = model.item as SurveyResult;
        render(html`${surveyResult.id}`, root);
    }

    _finishedOnRenderer(root: HTMLElement, _grid: GridElement, model: GridItemModel) {
        const surveyResult = model.item as SurveyResult;
        render(html`${surveyResult.endTime.substring(0, 10)} at ${surveyResult.endTime.substring(11, 16)}`, root);
    }

    _rowDetailsRenderer(root: HTMLElement, _grid: GridElement, model: GridItemModel) {
        const surveyResult = model.item as SurveyResult;
        render(html`
            ${surveyResult.survey.description}
        `, root);
    }
}

customElements.define('completed-surveys-grid', CompletedSurveysGrid);
