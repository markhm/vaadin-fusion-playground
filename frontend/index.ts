import { Flow } from '@vaadin/flow-frontend/Flow';
import { Commands, Context, Route, Router } from '@vaadin/router';

import './views/main/main-view';
import './views/login/login-view';
import './views/example/helloworld/hello-world-view';
import { handleAuthentication, isAuthenticated, signOut } from './auth';

import './global-styles';

const authGuard = async (context: Context, commands: Commands) => {
	if (!(await isAuthenticated())) {
		// Save requested path
		sessionStorage.setItem('login-redirect-path', context.pathname);
		return commands.redirect('/login');
	}
	return undefined;
};

const { serverSideRoutes } = new Flow({
	imports: () => import('../target/frontend/generated-flow-imports'),
});

const routes: Route[] = [
	{
		path: '/login',
		component: 'login-view'
	},
	{
		path: '/callback',
		action: async (_: Context, commands: Commands) => {
			if (await handleAuthentication()) {
				return commands.redirect(
					sessionStorage.getItem('login-redirect-path') || '/'
				);
			} else {
				return commands.redirect('/login?error');
			}
		},
	},
	{
		path: '/logout',
		action: async (_: Context, commands: Commands) => {
			signOut();
			location.reload();
			return commands.prevent();
		},
	},
	{
		path: '',
		component: 'main-view',
		// action: authGuard, // Require a logged in user to access
		children: [
			{
				path: '',
				component: 'introduction-view',
				action: async() => { await import ('./views/introduction/introduction-view')}
			},
			{
				path: 'about',
				component: 'about-view',
				action: async() => { await import ('./views/about/about-view')}
			},
			{
				path: 'terms-and-conditions',
				component: 'terms-and-conditions-view',
				action: async() => { await import ('./views/about/terms-and-conditions-view')}
			},
			{
				path: 'user-details',
				component: 'user-details-view',
				action: async (context: Context, commands: Commands) => {
					const authRedirect = await authGuard(context, commands);
					if (authRedirect) return authRedirect;
					await import('./views/user/user-details-view');
					return undefined;
				},
			},
			{
				path: 'create-account',
				component: 'create-account-view',
				action: async() => { await import ('./views/user/create-account-view')}
			},
			{
				path: 'select-survey',
				component: 'select-survey-view',
				action: async (context: Context, commands: Commands) => {
					const authRedirect = await authGuard(context, commands);
					if (authRedirect) return authRedirect;
					await import('./views/survey/select-survey-view');
					return undefined;
				},
			},
			{
				path: 'question',
				component: 'question-view',
				action: async (context: Context, commands: Commands) => {
					const authRedirect = await authGuard(context, commands);
					if (authRedirect) return authRedirect;
					await import('./views/question/question-view');
					return undefined;
				},
			},
			{
				path: 'confirm-responses',
				component: 'confirm-responses-view',
				action: async (context: Context, commands: Commands) => {
					const authRedirect = await authGuard(context, commands);
					if (authRedirect) return authRedirect;
					await import('./views/responses/confirm-responses-view');
					return undefined;
				},
			},
			{
				path: 'survey-results',
				component: 'survey-result-view',
				action: async (context: Context, commands: Commands) => {
					const authRedirect = await authGuard(context, commands);
					if (authRedirect) return authRedirect;
					await import('./views/responses/survey-result-view');
					return undefined;
				},
			},
			{
				path: 'completed-surveys',
				component: 'completed-surveys-view',
				action: async (context: Context, commands: Commands) => {
					const authRedirect = await authGuard(context, commands);
					if (authRedirect) return authRedirect;
					await import('./views/responses/completed-surveys-view');
					return undefined;
				},
			},
			{
				path: 'achievements',
				component: 'achievements-view',
				action: async (context: Context, commands: Commands) => {
					const authRedirect = await authGuard(context, commands);
					if (authRedirect) return authRedirect;
					await import('./views/achievement/achievements-view');
					return undefined;
				},
			},
			{
				path: 'my-surveys',
				component: 'my-surveys-view',
				action: async (context: Context, commands: Commands) => {
					const authRedirect = await authGuard(context, commands);
					if (authRedirect) return authRedirect;
					await import('./views/survey/my-surveys-view');
					return undefined;
				},
			},
			{
				path: 'edit-survey',
				component: 'edit-survey-view',
				action: async (context: Context, commands: Commands) => {
					const authRedirect = await authGuard(context, commands);
					if (authRedirect) return authRedirect;
					await import('./views/survey/edit-survey-view');
					return undefined;
				},
			},
			{
				path: 'edit-question',
				component: 'edit-question-view',
				action: async (context: Context, commands: Commands) => {
					const authRedirect = await authGuard(context, commands);
					if (authRedirect) return authRedirect;
					await import('./views/question/edit-question-view');
					return undefined;
				},
			},
			// {
			// 	path: 'create-account',
			// 	component: 'create-account-view',
			// 	action: async() => { await import ('./views/login/create-account-view')}
			// },
			{ path: 'hello', component: 'hello-world-view' },
			{
				path: 'fusion-examples',
				component: 'fusion-examples-view',
				action: async () => { await import('./views/example/fusion-examples-view'); },
			},
			{
				path: 'people',
				component: 'people-view',
				action: async () => { await import('./views/example/people/people-view'); },
			},
			{
				path: 'test/hello-world',
				component: 'hello-world-view',
				action: async () => { await import ('./views/example/helloworld/hello-world-view'); }
			},
			{
				path: 'events',
				component: 'events-view',
				action: async () => { await import ('./views/example/events/events-view'); }
			},
			...serverSideRoutes
		],
	},
];

export const router = new Router(document.querySelector('#outlet'));
router.setRoutes(routes);
