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
				path: 'surveys',
				component: 'surveys-view',
				action: async (context: Context, commands: Commands) => {
					const authRedirect = await authGuard(context, commands);
					if (authRedirect) return authRedirect;
					await import('./views/survey/surveys-view');
					return undefined;
				},
			},
			{
				path: 'questions',
				component: 'questions-view',
				action: async (context: Context, commands: Commands) => {
					const authRedirect = await authGuard(context, commands);
					if (authRedirect) return authRedirect;
					await import('./views/question/questions-view');
					return undefined;
				},
			},
			{
				path: 'responses',
				component: 'responses-view',
				action: async (context: Context, commands: Commands) => {
					const authRedirect = await authGuard(context, commands);
					if (authRedirect) return authRedirect;
					await import('./views/responses/responses-view');
					return undefined;
				},
			},
			{
				path: 'add-achievement',
				component: 'add-achievement-view',
				action: async (context: Context, commands: Commands) => {
					const authRedirect = await authGuard(context, commands);
					if (authRedirect) return authRedirect;
					await import('./views/achievement/add-achievement-view');
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
				path: 'create-account',
				component: 'create-account-view',
				action: async() => { await import ('./views/login/create-account-view')}
			},
			{ path: 'hello', component: 'hello-world-view' },
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
