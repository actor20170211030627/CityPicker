package com.actor.parsehtml;

import com.alibaba.fastjson.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * description: 从 html 中读取 地址列表
 * @author : 李大发
 * date       : 2020/5/12 on 16:07
 * @version 1.0
 */
public class ReadAddressFromHtml {

    /**
     * 2020年2月中华人民共和国县以上行政区划代码
     */
    private static final String HTML_PATH =
            "F:\\Android\\CityPicker\\parsehtml\\src\\main\\java\\com\\actor\\parsehtml" +
                    "\\202003301019.html";

    public static void main(String[] args) {
        File file = new File(HTML_PATH);
        try {
            Document doc = Jsoup.parse(file, "utf-8");
            //省/直辖市/市
            Elements provincesAndCitys = doc.getElementsByClass("xl7030721");
            //区县
            Elements countys = doc.getElementsByClass("xl7130721");

            List<ProvinceBean> shengs = new ArrayList<>();//省/直辖市
            List<ProvinceBean> shis = new ArrayList<>();//市
            //获取 "省/直辖市/市" 列表
            ProvinceBean node = null;
            for (Element province : provincesAndCitys) {
                //<td class="xl7030721">130200</td>
                //<td class="xl7030721"><span style="mso-spacerun:yes">&nbsp;</span>唐山市</td>
                List<Node> nodes = province.childNodes();
                for (Node node1 : nodes) {
                    String value = node1.toString();
                    if (!value.contains("<")) {
                        if (node == null) {
                            node = new ProvinceBean(Long.parseLong(value));
                        } else {
                            node.name = value;
                            if (node.id % 10000 == 0) {
                                //0000 省/直辖市
                                shengs.add(node);
                            } else {
                                shis.add(node);
                            }
                            node = null;
                        }
                        break;
                    }
                }
            }
            //对 "省/直辖市" 进行处理, 赋值 "直辖市/市"
            for (ProvinceBean shengShi : shengs) {
                for (ProvinceBean shi : shis) {
                    //130000 ~ 140000, 河北省的市
                    if (shi.id > shengShi.id && shi.id < shengShi.id + 10_000) {
                        shengShi.citys.add(new CityBean(shi.id, shi.name));
                    }
                }
            }
            //处理 "北京/天津" 等没有地级市, 只有 区 的情况
            for (ProvinceBean shengShi : shengs) {
                if (shengShi.citys.isEmpty()) {
                    shengShi.citys.add(new CityBean(shengShi.id, shengShi.name));
                }
            }

            //获取 "区县" 列表, 并且放入 "直辖市/市" 中
            //省/直辖市 之间: 间隔10000
            //市 之间间隔   : 1000/100
            //区县 之间间隔   : 个位数
            //
            //北京市: 110000   ->
            //宁夏回族自治区: 640000
            //新疆维吾尔自治区: 650000
            //乌鲁木齐市: 650100
            //克拉玛依市: 650200
            //
            //710000    台湾省
            //810000    香港特别行政区
            //820000    澳门特别行政区
            CountyBean county = null;
            for (Element area : countys) {
                /**
                 * area 可能的值:
                 * <td class="xl7130721">130202</td>
                 * <td class="xl7130721"><span style="mso-spacerun:yes">&nbsp;&nbsp; </span>路南区</td>
                 * <td class="xl7130721"><span lang="EN-US">659010</span></td>
                 * <td class="xl7130721"><span style="mso-spacerun:yes">&nbsp;&nbsp; </span>胡杨河市</td>
                 */
                List<Node> nodes = area.childNodes();
                /**
                 * node1 可能的值:
                 * 130202
                 * <span style="mso-spacerun:yes">&nbsp;&nbsp; </span>
                 * 路南区
                 * <span lang="EN-US">659010</span>
                 */
                for (Node node1 : nodes) {
                    String value = null;
                    if (node1.childNodeSize() == 0) {
                        /**
                         * value 可能的值:
                         * 130202
                         * 路南区
                         */
                        value = node1.toString();
                    } else {
                        /**
                         * s 可能的值:
                         * 2.&nbsp;&nbsp;
                         * 3.659010
                         */
                        String s = node1.childNode(0).toString();
                        try {
                            value = String.valueOf(Long.parseLong(s));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    if (value == null) {
                        continue;
                    }
                    if (county == null) {
                        try {
                            county = new CountyBean(Long.parseLong(value));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        county.name = value;
                        for (ProvinceBean sheng : shengs) {
                            //如果这个 "区县" 属于这个 "省/直辖市"
                            if (county.id > sheng.id && county.id < sheng.id + 10_000) {
                                List<CityBean> citys = sheng.citys;
                                //"北京/天津" 等没有地级市
                                if (citys.size() == 1) {
                                    citys.get(0).countys.add(county);
                                } else {
                                    //省 下面的多个 市
                                    for (int j = citys.size() - 1; j >= 0; j--) {
                                        //如果这个 "区县" 属于这个 "市"
                                        CityBean cityBean = citys.get(j);
                                        if (county.id > cityBean.id) {
                                            cityBean.countys.add(county);
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        county = null;
                    }
                    break;
                }
            }
            // TODO: 2020/5/12 呆湾的区不正确..
            //https://baike.baidu.com/item/%E5%8F%B0%E6%B9%BE/122340?fr=aladdin#5 呆湾
            ProvinceBean daiwanProvince = shengs.get(31);
            List<CityBean> daiwanCitys = daiwanProvince.citys;
            daiwanCitys.clear();
            CityBean taibeiCity = new CityBean(710100, "台北市");
            //https://www.cnblogs.com/gfweb/p/11118882.html
            taibeiCity.countys.add(new CountyBean(710101, "中正区"));
            taibeiCity.countys.add(new CountyBean(710102, "大同区"));
            taibeiCity.countys.add(new CountyBean(710103, "中山区"));
            taibeiCity.countys.add(new CountyBean(710104, "松山区"));
            taibeiCity.countys.add(new CountyBean(710105, "大安区"));
            taibeiCity.countys.add(new CountyBean(710106, "万华区"));
            taibeiCity.countys.add(new CountyBean(710107, "信义区"));
            taibeiCity.countys.add(new CountyBean(710108, "士林区"));
            taibeiCity.countys.add(new CountyBean(710109, "北投区"));
            taibeiCity.countys.add(new CountyBean(710110, "内湖区"));
            taibeiCity.countys.add(new CountyBean(710111, "南港区"));
            taibeiCity.countys.add(new CountyBean(710112, "文山区"));
            taibeiCity.countys.add(new CountyBean(710113, "其它区"));
            daiwanCitys.add(taibeiCity);

            //新北市: https://baike.baidu.com/item/%E6%96%B0%E5%8C%97/62332?fromtitle=%E6%96%B0%E5%8C%97%E5%B8%82&fromid=4737169#2
            CityBean xinbeiCity = new CityBean(710200, "新北市");
            String[] xinbeiCountys = {"板桥区", "汐止区", "新店区", "永和区", "中和区", "土城区",
                    "树林区", "三重区", "新庄区", "芦洲区", "瑞芳区", "三峡区", "莺歌区", "淡水区",
                    "万里区", "金山区", "深坑区", "石碇区", "平溪区", "双溪区", "贡寮区", "坪林区",
                    "乌来区", "泰山区", "林口区", "五股区", "八里区", "三芝区", "石门区"};
            for (String county1 : xinbeiCountys) {
                xinbeiCity.countys.add(new CountyBean(xinbeiCity.id, county1));
            }
            daiwanCitys.add(xinbeiCity);

            CityBean taoyuanCity = new CityBean(710300, "桃园市");
            taoyuanCity.countys.add(new CountyBean(710300, "桃园区"));
            taoyuanCity.countys.add(new CountyBean(710300, "中坜区"));
            taoyuanCity.countys.add(new CountyBean(710300, "平镇区"));
            taoyuanCity.countys.add(new CountyBean(710300, "八德区"));
            taoyuanCity.countys.add(new CountyBean(710300, "杨梅区"));
            taoyuanCity.countys.add(new CountyBean(710300, "芦竹区"));
            taoyuanCity.countys.add(new CountyBean(710300, "大溪区"));
            taoyuanCity.countys.add(new CountyBean(710300, "龙潭区"));
            taoyuanCity.countys.add(new CountyBean(710300, "龟山区"));
            taoyuanCity.countys.add(new CountyBean(710300, "大园区"));
            taoyuanCity.countys.add(new CountyBean(710300, "观音区"));
            taoyuanCity.countys.add(new CountyBean(710300, "新屋区"));
            taoyuanCity.countys.add(new CountyBean(710300, "复兴区"));
            daiwanCitys.add(taoyuanCity);

            CityBean taizhongCity = new CityBean(710400, "台中市");
            taizhongCity.countys.add(new CountyBean(710400, "中区"));
            taizhongCity.countys.add(new CountyBean(710400, "东区"));
            taizhongCity.countys.add(new CountyBean(710400, "西区"));
            taizhongCity.countys.add(new CountyBean(710400, "南区"));
            taizhongCity.countys.add(new CountyBean(710400, "北区"));
            taizhongCity.countys.add(new CountyBean(710400, "西屯区"));
            taizhongCity.countys.add(new CountyBean(710400, "南屯区"));
            taizhongCity.countys.add(new CountyBean(710400, "北屯区"));
            taizhongCity.countys.add(new CountyBean(710400, "丰原区"));
            taizhongCity.countys.add(new CountyBean(710400, "大里区"));
            taizhongCity.countys.add(new CountyBean(710400, "太平区"));
            taizhongCity.countys.add(new CountyBean(710400, "东势区"));
            taizhongCity.countys.add(new CountyBean(710400, "大甲区"));
            taizhongCity.countys.add(new CountyBean(710400, "清水区"));
            taizhongCity.countys.add(new CountyBean(710400, "沙鹿区"));
            taizhongCity.countys.add(new CountyBean(710400, "梧栖区"));
            taizhongCity.countys.add(new CountyBean(710400, "后里区"));
            taizhongCity.countys.add(new CountyBean(710400, "神冈区"));
            taizhongCity.countys.add(new CountyBean(710400, "潭子区"));
            taizhongCity.countys.add(new CountyBean(710400, "大雅区"));
            taizhongCity.countys.add(new CountyBean(710400, "新小区"));
            taizhongCity.countys.add(new CountyBean(710400, "石冈区"));
            taizhongCity.countys.add(new CountyBean(710400, "外埔区"));
            taizhongCity.countys.add(new CountyBean(710400, "大安区"));
            taizhongCity.countys.add(new CountyBean(710400, "乌日区"));
            taizhongCity.countys.add(new CountyBean(710400, "大肚区"));
            taizhongCity.countys.add(new CountyBean(710400, "龙井区"));
            taizhongCity.countys.add(new CountyBean(710400, "雾峰区"));
            taizhongCity.countys.add(new CountyBean(710400, "和平区"));
            daiwanCitys.add(taizhongCity);

            CityBean tainanCity = new CityBean(710500, "台南市");
            String[] tainanCountys = {"中西区", "东区", "南区", "北区", "安平区", "安南区", "永康区",
                    "归仁区", "新化区", "左镇区", "玉井区", "楠西区", "南化区", "仁德区", "关庙区",
                    "龙崎区", "官田区", "麻豆区", "佳里区", "西港区", "七股区", "将军区", "学甲区",
                    "北门区", "新营区", "后壁区", "白河区", "东山区", "六甲区", "下营区", "柳营区",
                    "盐水区", "善化区", "大内区", "山上区", "新市区", "安定区"};
            for (String tainanCounty : tainanCountys) {
                tainanCity.countys.add(new CountyBean(tainanCity.id, tainanCounty));
            }
            daiwanCitys.add(tainanCity);

            CityBean gaoxiongCity = new CityBean(710600, "高雄市");
            gaoxiongCity.countys.add(new CountyBean(gaoxiongCity.id, "旧高雄市区"));
            gaoxiongCity.countys.add(new CountyBean(gaoxiongCity.id, "凤山地区"));
            gaoxiongCity.countys.add(new CountyBean(gaoxiongCity.id, "冈山地区"));
            gaoxiongCity.countys.add(new CountyBean(gaoxiongCity.id, "旗山地区"));
            daiwanCitys.add(gaoxiongCity);


            //香港
            ProvinceBean gangProvince = shengs.get(32);
            List<CityBean> gangCitys = gangProvince.citys;
            gangCitys.clear();
            CityBean xianggangdaoCity = new CityBean(810100, "香港岛");
            xianggangdaoCity.countys.add(new CountyBean(810101, "中西区"));
            xianggangdaoCity.countys.add(new CountyBean(810102, "湾仔区"));
            xianggangdaoCity.countys.add(new CountyBean(810103, "东区"));
            xianggangdaoCity.countys.add(new CountyBean(810104, "南区"));
            gangCitys.add(xianggangdaoCity);

            CityBean jiulongCity = new CityBean(810200, "九龙半岛");
            jiulongCity.countys.add(new CountyBean(810201, "油尖旺区"));
            jiulongCity.countys.add(new CountyBean(810202, "深水埗区"));
            jiulongCity.countys.add(new CountyBean(810203, "九龙城区"));
            jiulongCity.countys.add(new CountyBean(810204, "黄大仙区"));
            jiulongCity.countys.add(new CountyBean(810205, "观塘区"));
            gangCitys.add(jiulongCity);

            CityBean cinjieCity = new CityBean(810300, "新界");
            cinjieCity.countys.add(new CountyBean(810301, "沙田区"));
            cinjieCity.countys.add(new CountyBean(810302, "荃湾区"));
            cinjieCity.countys.add(new CountyBean(810303, "葵青区"));
            cinjieCity.countys.add(new CountyBean(810304, "北区"));
            cinjieCity.countys.add(new CountyBean(810305, "大埔区"));
            cinjieCity.countys.add(new CountyBean(810306, "西贡区"));
            cinjieCity.countys.add(new CountyBean(810307, "屯门区"));
            cinjieCity.countys.add(new CountyBean(810308, "元朗区"));
            cinjieCity.countys.add(new CountyBean(810309, "离岛区"));
            gangCitys.add(cinjieCity);


            //https://baike.baidu.com/item/%E6%BE%B3%E9%97%A8/24335#4 澳门
            // TODO: 2020/5/13 澳门的区代码不对.
            ProvinceBean aoProvince = shengs.get(33);
            List<CityBean> aoCitys = aoProvince.citys;
            aoCitys.clear();
            CityBean aomenbandaoCity = new CityBean(820100, "澳门半岛");
            aomenbandaoCity.countys.add(new CountyBean(820100, "风顺堂区"));
            aomenbandaoCity.countys.add(new CountyBean(820100, "望德堂区"));
            aomenbandaoCity.countys.add(new CountyBean(820100, "大堂区"));
            aomenbandaoCity.countys.add(new CountyBean(820100, "圣安多尼堂区"));
            aomenbandaoCity.countys.add(new CountyBean(820100, "花地玛堂区"));
            aoCitys.add(aomenbandaoCity);

            CityBean dangzaiCity = new CityBean(820201, "氹仔");
            dangzaiCity.countys.add(new CountyBean(820201, "嘉模堂区"));
            aoCitys.add(dangzaiCity);

            CityBean luhuanCity = new CityBean(820202, "路环");
            luhuanCity.countys.add(new CountyBean(820202, "圣方济各堂区"));
            aoCitys.add(luhuanCity);

            String json = JSONObject.toJSONString(shengs);
            System.out.println("港澳台地区没有数据, 懒得弄, 还是去别的地方复制json吧...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
