package exif;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;


public class Main {

	public static int TYPE_PEOPLE = 0;
	public static int TYPE_LANDSCAPES = 1;
	
	public static String url_people = "https://tuchong.com/rest/ajax-tags/%25E4%25BA%25BA%25E5%2583%258F/posts?type=weekly&limit=100&page=1";
	public static String url_landscapes = "https://tuchong.com/rest/ajax-tags/%25E9%25A3%258E%25E5%2585%2589/posts?type=weekly&limit=20&page=1";
	
	public static String url_album_info = "https://tuchong.com/rest/posts/";
	
	public static URLResource resource = new URLResource();
	
	public static void main(String[] args) {
		
		

		StringBuffer people_albums_json = new StringBuffer();
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("albums_people")));
			String str = null;
			while((str=br.readLine())!=null) {
				people_albums_json.append(str);
			}
			br.close();
			
			Posts posts = new Gson().fromJson(people_albums_json.toString(), Posts.class);
			
			System.out.println("posts_count: "+ posts.posts.size());
			
			Iterator<Post> posts_it = posts.posts.iterator();
			while(posts_it.hasNext()) {
				Post post = posts_it.next();
				//String post_id = post.post_id;
				fetchAlbumInfo(post);
				save(post, TYPE_LANDSCAPES);
			}
			//save(posts, TYPE_PEOPLE);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void fetchAlbumInfo(Post post) {
		
		System.out.println("post_id: "+post.post_id);
		
		//String post_id = "13522595";
		String album_info_json = resource.get(url_album_info + post.post_id);
		
		post.images = new Gson().fromJson(album_info_json, Post.class).images;
		
		int count = 1;
		for(Image image: post.images) {
			if(image.exif.lens == null) continue;
			String lens = image.exif.lens.name;
			String[] lens_fields = lens.split(" ");
			for(String field: lens_fields) {
				//判断焦距
				if(field.contains("mm")) {
					image.exif.lens.length = field;
				} else if(field.contains("f/") || field.contains("F/") || field.contains("f") || field.contains("F")) {
					image.exif.lens.aperture = field;
				} else if(field.contains("Canon") || 
						field.contains("Nikkor") || 
						field.contains("Sigma") || 
						field.contains("Zeiss") || 
						field.contains("Tamron") ||
						field.contains("XF") ||
						field.contains("PENTAX") || 
						field.contains("SONY")) {
					image.exif.lens.brand = field;
				}
			}
			
			//System.out.println("image no " + count + " : " +image.exif.lens.name);
			count++;
		}
		//System.out.println(post.images.get(0).exif.lens.name);
	}
	
	public static void save(Post post, int type) {
		QueryHelper helper = new QueryHelper();
		
		List<Image> images = post.images;
		if(images == null || images.size() == 0 ) return;
		
		for(Image image : images) {
			if(image.exif.camera == null || image.exif.lens == null) continue;
			helper.save(post.post_id, image, type);
		}

		helper.close();
	}
	
	public static void save(Posts posts, int type) {
		
		QueryHelper helper = new QueryHelper();
		
		for(Post post: posts.posts) {
			List<Image> images = post.images;
			if(images == null || images.size() == 0 ) continue;
			
			for(Image image : images) {
				if(image.exif.camera == null || image.exif.lens == null) continue;
				helper.save(post.post_id, image, type);
			}
		}
		helper.close();
	}
	
	public static void analyze(Posts posts) {
		
	}
	
}
