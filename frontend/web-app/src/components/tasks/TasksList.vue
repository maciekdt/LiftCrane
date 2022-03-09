<template>
	<div class="listBox">
		<ul>
			<li v-for="task in taskList"  v-bind:key="task.id">
				<TaskCard :task="task"/> 
			</li>
		</ul>


	</div>
    
</template>


<script>
import TaskCard from './TaskCard.vue'
import tasksRepo from '../../repos/taskRepo'

export default {
	name: "TasksList",

	components: {
		TaskCard
	},

	computed: {
		taskList(){
			return tasksRepo.getAllTasks()
		}
	}
}
</script>



<style scoped>
ul {
	list-style-type: none;
	padding: 0;
}
li {
	margin-top: 1em;
	padding-left: .5em;
	padding-right: .5em;
}
</style>
