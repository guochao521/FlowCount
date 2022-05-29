-- 创建数据库
dorp database if exists wangguochao;
create database if not exists wangguochao;
use wangguochao;

-- 创建 t_user 观众表
create table t_user(
    userid bigint,
    sex string,
    age int,
    occupation string,
    zipcode string
) 
row format serde 'org.apache.hadoop.hive.serde2.RegexSerDe'
with serdeproperties('input.regex'='(.*)::(.*)::(.*)::(.*)::(.*)','output.format.string'='%1$s %2$s %3$s %4$s %5$s')
stored as textfile;


-- 创建 t_movie 电影表
create table t_movie(
    movieid bigint,
    moviename string,
    movietype string
)
row format serde 'org.apache.hadoop.hive.serde2.RegexSerDe'
with serdeproperties('input.regex'='(.*)::(.*)::(.*)::(.*)::(.*)','output.format.string'='%1$s %2$s %3$s %4$s %5$s')
stored as textfile;


-- 创建 t_rating 影评表
create table t_rating(
userid bigint,
movieid bigint,
rate double,
times string) 
row format serde 'org.apache.hadoop.hive.serde2.RegexSerDe' 
with serdeproperties('input.regex'='(.*)::(.*)::(.*)::(.*)','output.format.string'='%1$s %2$s %3$s %4$s')
stored as textfile;


-- 导入数据
load data local inpath "/data/hive/users.dat" into table t_user;
load data local inpath "/data/hive/movies.dat" into table t_movie;
load data local inpath "/data/hive/ratings.dat" into table t_rating;


-- 题目一：展示电影ID为2116这部电影各年龄段的平均影评分
select ur.age, avg(rt.rate)
from t_user ur
    join (
        select userid, rate
        from t_rating
        where movieid = 2116 -- 指定电影ID
    ) rt
    on ur.userid = rt.userid
group by ur.age;


-- 题目二：找出男性评分最高且评分次数超过50次的10部电影，展示电影名，平均影评分和评分次数。
select sex,moviename,avg_rate,total 
from (
    select ur.sex,avg(ra.rate) as avg_rate, mv.moviename,
        count(1) as total
    from(
        select sex,userid
        from t_user
        where sex = 'M' -- 男性信息
    ) ur 
        join t_rating rt on ur.userid = rt.userid -- 关联 评分表
    join t_movie mv on rt.movieid = mv.movieid -- 关联 电影表，获取电影评分信息
    group by ur.sex, mv.moviename  having count(1) > 50 -- 限定超过50次
) t
order by avg_rate desc -- 平均分倒叙排序
limit 10; -- 选取前10个

-- 题目三：找出影评次数最多的女士所给出最高分的10部电影的平均影评分，展示电影名和平均影评分
select mv.moviename, rt.avg_rate
from( -- 每个UUID，按评分和movieID 倒叙排序，取前10名
    select userid, movieid
    from(
        select userid, movieid, row_number() over (partition by userid order by rate desc, movieid desc) as rn
        from t_rating
    ) t
    where rn <= 10
) t1
    left semi join ( -- 计算女士中评分次数最多的uuid
        select userid, total
        from (
            select ur.userid, count(1) as total
            from (
                select sex, userid
                from t_user
                where sex = 'F' -- 指定女士性别
            ) ur 
                join t_rating ra on ur.userid = ra.userid
            group by ur.userid
        ) t
        order by total desc
        limit 1
    ) tt
    on tt.userid = t1.userid
    left join t_movie mv on t1.movieid = mv.movieid
    left join ( -- 每部电影的平均分
        select movieid, avg(rate) as avg_rate
        from t_rating
        group by movieid
    ) rt
    on mv.movieid = rt.movieid
order by rt.avg_rate desc; -- 按平均分倒叙排序
