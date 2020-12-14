create table chessplayer_activationcodes
(
	activation_code_id int identity constraint PK_chessplayer_activationcodes primary key,
	chessplayer_id int not null,
	activation_code nvarchar(20) not null,
	valid_date datetime
);