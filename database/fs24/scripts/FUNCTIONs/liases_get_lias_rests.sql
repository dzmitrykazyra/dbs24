CREATE OR REPLACE FUNCTION liases_get_lias_rests(
	p_lias_id tidcode)
    RETURNS refcursor
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

 declare default_cursor refcursor;

begin open default_cursor for 
SELECT lr.* 
  FROM  LiasRests lr 
  WHERE lr.lias_id= p_lias_id 
    and lr.rest_type=1; 

return(default_cursor);
end;

$BODY$;