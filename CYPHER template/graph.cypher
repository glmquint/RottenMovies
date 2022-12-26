user{
	id: uuid
}

topCritic{
	id: uuid
	name: String
}

movie{
	id: uuid
	title: String
}

user - (follows) -> topCritic{
}

user/topCritic - (reviewed) -> movie {
	freshness: bool
	date: Date
}