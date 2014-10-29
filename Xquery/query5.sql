SELECT count(R1.Reviewer) AS "COUNT", R1.Reviewer, ROUND(AVG(CAST(R1.rating as float)), 3) AS "AVERAGE RATING"
FROM REVIEW R, 
XMLTABLE('/REVIEWS/Review' passing R.REVIEWXML
                          columns Reviewer VARCHAR(100) PATH 'Reviewer', 
                                  Rating VARCHAR(150) PATH 'Rating') R1
group by R1.Reviewer
order by avg(R1.Rating) desc;
