import { Flow } from '@vaadin/flow-frontend/Flow';
import { Router } from '@vaadin/router';
import { isAuthenticated } from "../frontend/utils/security";

import './global-styles';

const COOKIE_DOMAIN = 'https://vaadin-fusion-playground.herokuapp.com';

const { serverSideRoutes } = new Flow({
  imports: () => import('../target/frontend/generated-flow-imports'),
});

let routes;

if (isAuthenticated(COOKIE_DOMAIN)) {

	console.log('At index.ts: User is authenticated, approving all routes.');

	routes = [
		// for client-side, place routes below (more info https://vaadin.com/docs/v15/flow/typescript/creating-routes.html)
		{
			path: '',
			component: 'main-view',
			action: async () => { await import ('./views/main/main-view'); },
			children: [
				{
					path: 'account/create',
					component: 'person-form-view',
					action: async () => { await import ('./views/personform/person-form-view'); }
				},
				{
					path: 'test/hello-world',
					component: 'hello-world-view',
					action: async () => { await import ('./views/helloworld/hello-world-view'); }
				},
				{
					path: 'events',
					component: 'events-view',
					action: async () => { await import ('./views/events/events-view'); }
				},
				{
					path: 'question',
					component: 'question-view',
					action: async () => { await import ('./views/question/question-view'); }
				},
				// for server-side, the next magic line sends all unmatched routes:
				...serverSideRoutes // IMPORTANT: this must be the last entry in the array
			]
		},
	];
}
else {

	console.log('At index.ts: No, user is NOT authenticated.');

	routes = [
		// for client-side, place routes below (more info https://vaadin.com/docs/v15/flow/typescript/creating-routes.html)
		{
			path: '',
			component: 'main-view',
			action: async () => { await import ('./views/main/main-view'); },
			children: [
				{
					path: 'account/create',
					component: 'person-form-view',
					action: async () => { await import ('./views/personform/person-form-view'); }
				},
				// for server-side, the next magic line sends all unmatched routes:
				...serverSideRoutes // IMPORTANT: this must be the last entry in the array
			]
		},
	];
}

export const router = new Router(document.querySelector('#outlet'));
router.setRoutes(routes);
