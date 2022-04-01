import { createRouter, createWebHashHistory } from 'vue-router';
import LoginForm from '../components/login/LoginForm.vue'
import TasksList from '../components/tasks/TasksList.vue'
import ReviewForm from '../components/reviews/ReviewForm'
import ReviewCamera from '../components/reviews/ReviewCamera'

export default createRouter({
    history: createWebHashHistory(),

    routes: [{
            path: '/',
            name: "LoginForm",
            component: LoginForm
        },
        {
            path: '/tasks',
            name: "TasksList",
            component: TasksList
        },
        {
            path: '/review',
            name: "ReviewForm",
            component: ReviewForm
        },
        {
            path: '/review-camera',
            name: "ReviewCamera",
            component: ReviewCamera
        }
    ]
})