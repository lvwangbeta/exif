图虫网 API
===========

人像热门排行：https://tuchong.com/rest/ajax-tags/%25E4%25BA%25BA%25E5%2583%258F/posts?type=weekly&limit=20&page=1

风光热门排行：https://tuchong.com/rest/ajax-tags/%25E9%25A3%258E%25E5%2585%2589/posts?type=weekly&limit=20&page=1

limit：每页相册数量 
page：页码


相册数据：https://tuchong.com/rest/posts/13538713

13538713为相册id

### 数据库初始化SQL 

	create table exif(
	    album_id varchar(20),
	    photo_id varchar(20),
	    photo_type integer,
	    lens varchar(300),
	    camera varchar(100),
	    brand varchar(30),
	    length varchar(30),
	    aperture varchar(30),
	    category varchar(10)
	    
	);
