<template>
	<div class="page">
		<div class="topAppBar">
			<table><tr>
				<th class="menuIconCell"><img src='../../assets/menu-icon.png' class="menuIcon"/></th>
				<th><p class="appBarText">Serwisy</p></th>
				<th class="filterIconCell"><img src='../../assets/filter-icon--.png' class="filterIcon"/></th>
			</tr></table>
		</div>
		<div class="listBox">
			<ul>
				<li v-for="task in taskList"  v-bind:key="task.id">
					<TaskCard :task="task"/> 
				</li>
			</ul>
		</div>
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
	max-height: 100%;
	list-style-type: none;
	padding: 0;
}
li {
	margin-top: 1em;
	padding-left: .8em;
	padding-right: .8em;
}
table {
	height: 100%;
	width: 100%;
}

.page {
	max-height: 100%;
	width: 100%;
}
.listBox{
	height: calc(100% - 3.5em);
	width: 100%;
	overflow-y:auto;
	position: absolute;
}
.topAppBar {
	background-color: rgb(54, 108, 223);
	height: 3.5em;
	width: 100%;
	top: 0;
}
.appBarText {
	color: white;
	font-weight: bold;
	text-align: left;
	margin-left: .2em;
	font-size: 1.15em;
}
.menuIcon {
	height: 2.4em;
}
.menuIconCell {
	width: 3.5em;
	padding-left: .1em;
}
.filterIcon {
	height: 2.2em;
}
.filterIconCell {
	width: 3em;
	padding-right: .7em;
}



</style>
