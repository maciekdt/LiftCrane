import { createRouter, createWebHashHistory } from 'vue-router';
import LoginForm from '../components/login/LoginForm.vue'
import TasksList from '../components/tasks/TasksList.vue'

export default createRouter({
    history: createWebHashHistory(),

    routes: [{
            path: '/login',
            name: "LoginForm",
            component: LoginForm
        },
        {
            path: '/tasks',
            name: "TasksList",
            component: TasksList
        }
    ]
})