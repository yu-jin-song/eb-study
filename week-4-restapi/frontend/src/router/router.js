import { createWebHistory, createRouter } from 'vue-router'
import PostList from '../controller/PostList.vue'
import PostView from '../controller/PostView.vue'
import PostWriteForm from '../controller/PostWriteForm.vue'
import PostModifyForm from '../controller/PostModifyForm.vue'

// 공통 경로 변수 정의
const boardPath = '/boards/free';

const routes = [
    {
        path: '/',
        redirect: `${boardPath}/list`    // 메인 화면으로 이동할 경로 설정
    },
    {
        path: `${boardPath}/list`,
        name: 'PostList',
        component: PostList
    },
    {
        path: `${boardPath}/view/:id`,
        name: 'PostView',
        component: PostView
    },
    {
        path: `${boardPath}/write`,
        name: 'PostWriteForm',
        component: PostWriteForm
    },
    {
        path: `${boardPath}/modify/:id`,
        name: 'PostModifyForm',
        component: PostModifyForm
    }
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router;