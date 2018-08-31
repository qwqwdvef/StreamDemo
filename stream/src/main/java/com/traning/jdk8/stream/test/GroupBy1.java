package com.traning.jdk8.stream.test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.traning.jdk8.stream.util.FileUtils;


/**
 * 读取哈姆雷特剧本.txt
 * 基于Stream流式处理，识别出每个主要角色说了多少句话，不包括旁白
 *
 */
public class GroupBy1 {
	
	
	public String getRoleName(String line){
		
		String a = line.replaceAll((char)12288+""," ").replaceAll("\\s+", " ");
		
		String b = a.length()>1?a.substring(1):"";
		
		return b.contains(" ")?b.substring(0,b.indexOf(" ")):b;
	}
	
	public Stream<String> getTalker(String line){
		
		String a = line.substring(0, line.indexOf(":")).replaceAll("\\s", "").replaceAll((char)12288+"","");
		a = a.length()>3?"":a;
		if(line.contains("、")){
			return Stream.of(a.split("、"));
		}else{
			return Stream.of(a);
		}
	}
	
	public static boolean filterNoPersion(String name){
		
		Set<String> exclude = new HashSet<String>();
		exclude.add("全体人");
		exclude.add("译者注");
		exclude.add("她说");
		exclude.add("正是");
		exclude.add("二人");
		exclude.add("[读信");
		exclude.add("致词者");
		exclude.add("他回答");
		exclude.add("『赫兄");
		exclude.add("你听著");
		exclude.add("他会说");
		
		return !"".equals(name) && !name.contains("景") && !exclude.contains(name);
	}
	
	public static void main(String[] args) {
		
		GroupBy1 g = new GroupBy1();
		
		Stream<String> lines = FileUtils.load("f:\\hamu.txt");

		if(lines == null)  return;
		
		Map<String, String> roleMap = lines.limit(56).skip(8)  //截取角色列表
				.map(v->g.getRoleName(v))  //对流里的值，再做处理
				.filter(v->v.length()>0)
				.collect(Collectors.toMap(v-> v.substring(0,1)  //Map所映射的key
								,v->v  //Map的value
									,(a,b)->a  //如果key相同，多个value的处理方式
										,()->new TreeMap<String, String>()));  //Map的类型
			
		lines = FileUtils.load("f:\\hamu.txt");
		
		if(lines == null)  return;
		
		
		
		Map<String, Long> nameNumMap = lines.filter(v->v.contains(":"))  //根据:来判断角色开口说话
				.flatMap(v->g.getTalker(v))  //截取角色名称
					.filter(GroupBy1::filterNoPersion)  //过滤不是主角的人
						.collect(Collectors.groupingBy(v->{return roleMap.containsKey(v)?roleMap.get(v):v;},Collectors.counting()));  
		
	   nameNumMap.entrySet().stream()
				.sorted((v1,v2)->{return v1.getValue() < v2.getValue()?1:v1.getValue() == v2.getValue()?0:-1;})
					.forEach(a->System.out.println(a.getKey()+",开了"+a.getValue()+"次口"));
	}
	
}
