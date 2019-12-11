package com.example.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class Temp {

    public static void main(String[] args) {

    }

    @Test
    public void temp() {
        String positionStr = "{\n" +
                "\t\"class\": \"GraphLinksModel\",\n" +
                "\t\"nodeDataArray\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"category\": \"START\",\n" +
                "\t\t\t\"source\": \"/static/img/equ.png\",\n" +
                "\t\t\t\"equipment_id\": \"59-YS-1005\",\n" +
                "\t\t\t\"name\": \"中间轴热前自动连线辅助装置\",\n" +
                "\t\t\t\"text\": \"59-YS-1005\",\n" +
                "\t\t\t\"useFlag\": 1,\n" +
                "\t\t\t\"ShowBottleneck\": false,\n" +
                "\t\t\t\"key\": -29,\n" +
                "\t\t\t\"loc\": \"-115 79\",\n" +
                "\t\t\t\"group\": -2\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"isGroup\": true,\n" +
                "\t\t\t\"category\": \"OfNodes\",\n" +
                "\t\t\t\"text\": \"热前中间轴线\",\n" +
                "\t\t\t\"key\": -2,\n" +
                "\t\t\t\"unid\": \"aa20e4fc82c544868f5aa176f18da698\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"category\": \"START\",\n" +
                "\t\t\t\"source\": \"/static/img/equ.png\",\n" +
                "\t\t\t\"equipment_id\": \"59-DB-1004\",\n" +
                "\t\t\t\"name\": \"打标机\",\n" +
                "\t\t\t\"text\": \"59-DB-1004\",\n" +
                "\t\t\t\"useFlag\": 1,\n" +
                "\t\t\t\"ShowBottleneck\": false,\n" +
                "\t\t\t\"key\": -17,\n" +
                "\t\t\t\"loc\": \"37 80\",\n" +
                "\t\t\t\"group\": -2\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"category\": \"START\",\n" +
                "\t\t\t\"source\": \"/static/img/equ.png\",\n" +
                "\t\t\t\"equipment_id\": \"59-Y-1029\",\n" +
                "\t\t\t\"name\": \"数控滚齿机\",\n" +
                "\t\t\t\"text\": \"59-Y-1029\",\n" +
                "\t\t\t\"useFlag\": 1,\n" +
                "\t\t\t\"ShowBottleneck\": false,\n" +
                "\t\t\t\"key\": -62,\n" +
                "\t\t\t\"loc\": \"190 30\",\n" +
                "\t\t\t\"group\": -2,\n" +
                "\t\t\t\"isBottleneck\": 1\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"category\": \"START\",\n" +
                "\t\t\t\"source\": \"/static/img/equ.png\",\n" +
                "\t\t\t\"equipment_id\": \"59-Y-1030\",\n" +
                "\t\t\t\"name\": \"数控滚齿机\",\n" +
                "\t\t\t\"text\": \"59-Y-1030\",\n" +
                "\t\t\t\"useFlag\": 1,\n" +
                "\t\t\t\"ShowBottleneck\": false,\n" +
                "\t\t\t\"key\": -169,\n" +
                "\t\t\t\"loc\": \"192 118\",\n" +
                "\t\t\t\"group\": -2,\n" +
                "\t\t\t\"isBottleneck\": 1\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"linkDataArray\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"from\": -29,\n" +
                "\t\t\t\"to\": -17,\n" +
                "\t\t\t\"points\": [\n" +
                "\t\t\t\t-55,\n" +
                "\t\t\t\t79,\n" +
                "\t\t\t\t-45,\n" +
                "\t\t\t\t79,\n" +
                "\t\t\t\t-39,\n" +
                "\t\t\t\t79,\n" +
                "\t\t\t\t-39,\n" +
                "\t\t\t\t80,\n" +
                "\t\t\t\t-33,\n" +
                "\t\t\t\t80,\n" +
                "\t\t\t\t-23,\n" +
                "\t\t\t\t80\n" +
                "\t\t\t]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"from\": -17,\n" +
                "\t\t\t\"to\": -62,\n" +
                "\t\t\t\"points\": [\n" +
                "\t\t\t\t97,\n" +
                "\t\t\t\t80,\n" +
                "\t\t\t\t107,\n" +
                "\t\t\t\t80,\n" +
                "\t\t\t\t113.5,\n" +
                "\t\t\t\t80,\n" +
                "\t\t\t\t113.5,\n" +
                "\t\t\t\t30,\n" +
                "\t\t\t\t120,\n" +
                "\t\t\t\t30,\n" +
                "\t\t\t\t130,\n" +
                "\t\t\t\t30\n" +
                "\t\t\t]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"from\": -17,\n" +
                "\t\t\t\"to\": -169,\n" +
                "\t\t\t\"points\": [\n" +
                "\t\t\t\t97,\n" +
                "\t\t\t\t80,\n" +
                "\t\t\t\t107,\n" +
                "\t\t\t\t80,\n" +
                "\t\t\t\t114.5,\n" +
                "\t\t\t\t80,\n" +
                "\t\t\t\t114.5,\n" +
                "\t\t\t\t118,\n" +
                "\t\t\t\t122,\n" +
                "\t\t\t\t118,\n" +
                "\t\t\t\t132,\n" +
                "\t\t\t\t118\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";

        System.out.println(t(positionStr));
    }

    public String t(String positionStr) {
        BaseResult baseResult = new BaseResult(200, "success");

        JSONObject positionJSONObject = JSONObject.parseObject(positionStr);
        // 参数
        Object nodeDataArray = positionJSONObject.get("nodeDataArray");
        // 坐标  String position = "{\"linkDataArray\":[{\"from\":3,\"to\":4},{\"from\":1,\"to\":4},{\"from\":1,\"to\":3},{\"from\":2,\"to\":3},{\"from\":2,\"to\":7},{\"from\":4,\"to\":5},{\"from\":4,\"to\":6}]}";
        Object linkDataArray = positionJSONObject.get("linkDataArray");
        if (nodeDataArray == null) {
            return JSONObject.toJSONString(baseResult.setCode(400).setMessage("获取绑定参数有误"), SerializerFeature.WriteMapNullValue);

        } else {
            JSONArray nodeDataJSONArr = JSONObject.parseArray(nodeDataArray.toString());
            JSONArray linkDataJSONArr = JSONObject.parseArray(linkDataArray.toString());

            // 解析数据
            List<FEamFactoryModePosition> fEamFactoryModePositions = parseNodeData(nodeDataJSONArr);
            List<Integer[]> sites = parseLinkData(linkDataJSONArr);

            // 将设备和坐标按产线划分，并检测是否有设备未添加到产线
            // 产线
            Map<Integer, FEamFactoryModePosition> prodLines = new HashedMap();
            // 设备
            Map<Integer, List<FEamFactoryModePosition>> equipments = new HashedMap();
            // 坐标
            Map<Integer, List<Integer[]>> positions = new HashedMap();

            for (int i = 0; i < fEamFactoryModePositions.size(); i++) {
                FEamFactoryModePosition fEamFactoryModePosition = fEamFactoryModePositions.get(i);
                Boolean isGroup = fEamFactoryModePosition.getIsGroup();
                Integer group = fEamFactoryModePosition.getGroup();
                Integer key = fEamFactoryModePosition.getKey();
                if (isGroup != null && isGroup == true) {
                    prodLines.put(key, fEamFactoryModePosition);
                } else if (group != null) {
                    List<FEamFactoryModePosition> temp = equipments.get(group);
                    if (temp == null || temp.size() < 1) {
                        equipments.put(group, new ArrayList<FEamFactoryModePosition>() {{
                            add(fEamFactoryModePosition);
                        }});
                        positions.put(group, new ArrayList<Integer[]>() {{
                            addAll(sites.stream().filter(t -> (t[0] == key || t[1] == key)).collect(Collectors.toList()));
                        }});
                    } else {
                        temp.add(fEamFactoryModePosition);
                        equipments.put(group, temp);
                        List<Integer[]> prodLinePositions = positions.get(group);
                        prodLinePositions.addAll(sites.stream().filter(t -> (t[0] == key || t[1] == key)).collect(Collectors.toList()));
                        prodLinePositions = prodLinePositions.stream().distinct().collect(Collectors.toList());
                        positions.put(group, prodLinePositions);
                    }
                } else {
                    return JSONObject.toJSONString(baseResult.setCode(100).setMessage("检测到存在设备未添加到产线"), SerializerFeature.WriteMapNullValue);
                }
            }

            List<Integer> lineKeys = new ArrayList<>();
            Iterator<Integer> prodListIt = prodLines.keySet().iterator();
            while (prodListIt.hasNext()) {
                Integer lineKey = prodListIt.next();
                FEamFactoryModePosition line = prodLines.get(lineKey);
                if (org.springframework.util.StringUtils.isEmpty(line.getName())) {
                    return JSONObject.toJSONString(baseResult.setCode(100).setMessage("产线名称不可为空"), SerializerFeature.WriteMapNullValue);
                }
//                // 去除数据库中没有的空产线
//                if (!equipments.keySet().contains(lineKey)) {
//                    if (!org.springframework.util.StringUtils.isEmpty(line.getUnid())) {
//                        Integer countLine = fEamProdLineService.selectCount(new EntityWrapper<FEamProdLine>().eq("delete_flag", 0).eq("use_flag", 1));
//                        if (countLine != null && countLine > 0) {
//                            return JSONObject.toJSONString(baseResult.setCode(100).setMessage("检测到产线下无设备"), SerializerFeature.WriteMapNullValue);
//                        }
//                    }
//                    lineKeys.add(lineKey);
//                }
            }
//            // 去除数据库中没有的空产线
//            if (lineKeys.size() > 0) {
//                for (int i = 0; i < nodeDataJSONArr.size(); i++) {
//                    JSONObject nDataT = (JSONObject) nodeDataJSONArr.get(i);
//                    Integer key = nDataT.getInteger("key");
//                    if (lineKeys.contains(key)) {
//                        nodeDataJSONArr.remove(i);
//                    }
//                }
//                lineKeys.forEach(t -> prodLines.remove(t));
//            }
            Map<String, Object> positionMap = new HashMap<>();
            positionMap.put("class", positionJSONObject.getString("class"));
            positionMap.put("nodeDataArray", nodeDataJSONArr);
            positionMap.put("linkDataArray", linkDataArray);

            Iterator<Integer> it = equipments.keySet().iterator();
            while (it.hasNext()) {
                Integer lineKey = it.next();
                if (equipments.get(lineKey).size() > 1 && positions.get(lineKey).size() < 1) {
                    return JSONObject.toJSONString(baseResult.setCode(100).setMessage("请检查产线连线"), SerializerFeature.WriteMapNullValue);
                }
            }

            // 串并联数据
            Map<String, List<String>> equipRelation = new HashedMap();
            // 检查每条产线是否符合要求
            Iterator<Integer> iterator = positions.keySet().iterator();
            while (iterator.hasNext()) {
                Integer line = iterator.next();
                List<Integer[]> prodLinePositions = positions.get(line);

                List<FEamFactoryModePosition> prodLineEquips = equipments.get(line);
                if (prodLineEquips != null && prodLineEquips.size() > 0) {
                    // --------- 检测瓶颈是否存在设备 --------- //
                    boolean bottleFlag = true;
                    for (int i = 0; i < prodLineEquips.size(); i++) {
                        Integer isBottleneck = prodLineEquips.get(i).getIsBottleneck();
                        if (isBottleneck != null && isBottleneck == 1) {
                            bottleFlag = false;
                            break;
                        }
                    }
                    if (bottleFlag) {
                        return JSONObject.toJSONString(baseResult.setCode(100).setMessage("产线中无瓶颈设备"), SerializerFeature.WriteMapNullValue);
                    }

                    // --------- 检测跨产线连线 --------- //
                    for (int i = 0; i < prodLinePositions.size(); i++) {
                        boolean flag = false;
                        for (int j = 0; j < prodLineEquips.size(); j++) {
                            if (prodLineEquips.get(j).getKey().intValue() == prodLinePositions.get(i)[1].intValue()) {
                                break;
                            }
                            if (j == prodLineEquips.size() - 1) {
                                flag = true;
                            }
                        }
                        if (flag) {
                            return JSONObject.toJSONString(baseResult.setCode(100).setMessage("检测跨产线连线"), SerializerFeature.WriteMapNullValue);
                        }
                    }
                }

                // --------- 检测一条产线中，唯一连线 --------- //
                // 结构化数据，构造父子节点在同一数组
                // {1=[1, 4, 3], 2=[2, 3, 7], 3=[3, 4], 4=[4, 5, 6]}
                Map<Integer, List<Integer>> parentChildrenMap = getParentChildren(prodLinePositions);
                // [[1, 4, 3], [2, 3, 7], [3, 4], [4, 5, 6]]
                List<List<Integer>> parentChildrenList = new ArrayList<>(parentChildrenMap.values());


                // --------- 检测一条产线中有未连通设备 --------- //
                List<Integer> linkDataParentChildrenList = parentChildrenList.stream().reduce(new ArrayList<>(), (list, y) -> {
                    list.addAll(y);
                    return list;
                });
                if (linkDataParentChildrenList != null
                        && linkDataParentChildrenList.size() > 0
                        && equipments.get(line).stream()
                        .map(FEamFactoryModePosition::getKey)
                        .distinct()
                        .filter(x -> !linkDataParentChildrenList.contains(x)).count() > 0) {
                    return JSONObject.toJSONString(baseResult.setCode(100).setMessage("检测一条产线中有未连通设备"), SerializerFeature.WriteMapNullValue);
                }

                // 判断确定为一条产线
                if (parentChildrenList.size() > 1) {
                    // 筛选判断多条产线
                    List<Boolean> juggleFlag = juggleMultiLine(parentChildrenList);
                    // 如果出现false 则说明为多条产线
                    if (juggleFlag.contains(false)) {
                        return JSONObject.toJSONString(baseResult.setCode(100).setMessage("检测出多条产线"), SerializerFeature.WriteMapNullValue);
                    }
                }

                // --------- 检测两节点循环连线 --------- //
                boolean loopFlag = false;
                // [[3, 4], [1, 4], [1, 3], [2, 3], [2, 7], [4, 5], [4, 6]]
                List<List<Integer>> parentChild = prodLinePositions.stream().map(t -> Arrays.asList(t)).collect(Collectors.toList());
                for (int i = 0; i < parentChild.size() - 1; i++) {
                    List<Integer> pc = parentChild.get(i);
                    Integer p = pc.get(0);
                    Integer c = pc.get(1);
                    if (p == c) {
                        loopFlag = true;
                        break;
                    }
                    for (int j = i + 1; j < parentChild.size(); j++) {
                        if (p == parentChild.get(j).get(1) && c == parentChild.get(j).get(0)) {
                            loopFlag = true;
                            break;
                        }
                    }
                }
                if (loopFlag) {
                    return JSONObject.toJSONString(baseResult.setCode(100).setMessage("检测出单节点循环连线"), SerializerFeature.WriteMapNullValue);
                }


                // --------- 检测产线内跨节点连线 --------- //
                for (int i = 0; i < parentChild.size(); i++) {
                    if (getTheSameParentLine(parentChild.get(i).get(0), parentChild.get(i).get(1), parentChild)) {
                        return JSONObject.toJSONString(baseResult.setCode(100).setMessage("检测出跨节点连线"), SerializerFeature.WriteMapNullValue);
                    }
                }

                // 串并联关系(添加 linkData里面连线的设备)
                equipRelation.putAll(getEquipRelation(fEamFactoryModePositions, prodLinePositions, line));
            }

        }

        return "success";
    }


    /**
     * 解析 nodeData
     *
     * @param nodeDataJSONArr
     * @return
     */
    private List<FEamFactoryModePosition> parseNodeData(JSONArray nodeDataJSONArr) {
        List<FEamFactoryModePosition> fEamFactoryModePositions = new ArrayList<>();
        nodeDataJSONArr.forEach(t -> {
            JSONObject jsonObject = (JSONObject) t;
            FEamFactoryModePosition fEamFactoryModePosition = new FEamFactoryModePosition();
            fEamFactoryModePosition.setIsGroup(jsonObject.getBoolean("isGroup"));
            fEamFactoryModePosition.setUnid(jsonObject.getString("unid"));
            fEamFactoryModePosition.setCategory(jsonObject.getString("category"));
            fEamFactoryModePosition.setKey(jsonObject.getInteger("key"));
            fEamFactoryModePosition.setSource(jsonObject.getString("source"));
            fEamFactoryModePosition.setEquipment_id(jsonObject.getString("equipment_id"));
            fEamFactoryModePosition.setName(jsonObject.getString("text"));
            fEamFactoryModePosition.setLoc(jsonObject.getString("loc"));
            fEamFactoryModePosition.setGroup(jsonObject.getInteger("group"));
            fEamFactoryModePosition.setStatus(jsonObject.getString("status"));
            fEamFactoryModePosition.setUseFlag((jsonObject.getInteger("useFlag") == null || jsonObject.getInteger("useFlag") == 0) ? 0 : 1);
            fEamFactoryModePosition.setIsBottleneck((jsonObject.getInteger("isBottleneck") == null || jsonObject.getInteger("isBottleneck") == 0) ? 0 : 1);
            fEamFactoryModePositions.add(fEamFactoryModePosition);
        });
        return fEamFactoryModePositions;
    }

    /**
     * 解析 linkData
     *
     * @param linkDataJSONArr
     * @return
     */
    private List<Integer[]> parseLinkData(JSONArray linkDataJSONArr) {
        List<Integer[]> sites = new ArrayList<>();
        linkDataJSONArr.forEach(t -> {
            JSONObject jsonObject = (JSONObject) t;
            Integer from = jsonObject.getInteger("from");
            Integer to = jsonObject.getInteger("to");
            sites.add(new Integer[]{from, to});
        });
        return sites;
    }

    /**
     * 结构化数据，筛选父子节点
     */
    private Map<Integer, List<Integer>> getParentChildren(List<Integer[]> prodLinePositions) {
        Map<Integer, List<Integer>> linkDataConstructMap = new HashMap<>();
        List<Integer> tos;
        for (int i = 0; i < prodLinePositions.size(); i++) {
            Integer from = prodLinePositions.get(i)[0];
            Integer to = prodLinePositions.get(i)[1];
            if (linkDataConstructMap.containsKey(from)) {
                tos = linkDataConstructMap.get(from);
                if (!tos.contains(to)) {
                    tos.add(to);
                    linkDataConstructMap.put(from, tos);
                }
            } else {
                linkDataConstructMap.put(from, new ArrayList<Integer>() {{
                    add(from);
                    add(to);
                }});
            }
        }
        return linkDataConstructMap;
    }

    /**
     * 结构化数据，串并联
     */
    private Map<String, List<String>> getEquipRelation(List<FEamFactoryModePosition> fEamFactoryModePositions, List<Integer[]> prodLinePositions, Integer key) {
        Map<String, List<String>> linkDataConstructMap = new HashMap<>();

        // key 转成 设备名称
        List<String[]> equipments = new ArrayList<>();
        prodLinePositions.forEach(t -> {
            String[] p = {
                    fEamFactoryModePositions
                            .stream()
                            .filter(x -> x.getKey() == t[0])
                            .map(FEamFactoryModePosition::getEquipment_id)
                            .distinct()
                            .limit(1)
                            .reduce("", String::concat),
                    fEamFactoryModePositions
                            .stream()
                            .filter(y -> y.getKey() == t[1])
                            .map(FEamFactoryModePosition::getEquipment_id)
                            .distinct()
                            .limit(1)
                            .reduce("", String::concat)
            };
            equipments.add(p);
        });

        List<String> froms;
        for (int i = 0; i < equipments.size(); i++) {
            String from = equipments.get(i)[0];
            String to = equipments.get(i)[1];
            if (linkDataConstructMap.containsKey(to)) {
                froms = linkDataConstructMap.get(to);
                if (!froms.contains(from)) {
                    froms.add(from);
                    linkDataConstructMap.put(to, froms);
                }
            } else linkDataConstructMap.put(to, new ArrayList<>(Arrays.asList(from)));
        }

        return linkDataConstructMap;
    }

    /**
     * 筛选判断多条产线
     */
    private List<Boolean> juggleMultiLine(List<List<Integer>> parentChildrenList) {
        // 初始化标记
        List<Boolean> juggleFlag = new ArrayList<Boolean>() {{
            for (int i = 0; i < parentChildrenList.size(); i++) add(false);
        }};

        // 筛选，去重
        for (int i = 0; i < parentChildrenList.size() - 1; i++) {
            List<Integer> extractList = parentChildrenList.get(i);
            for (int j = i + 1; j < parentChildrenList.size(); j++) {
                if (parentChildrenList.get(j).removeAll(extractList)) {
                    juggleFlag.set(i, true);
                    juggleFlag.set(j, true);
                }
            }
        }
        return juggleFlag;
    }

    /**
     * 查找同父节点，不同子节点
     */
    private boolean getTheSameParentLine(Integer key, Integer val, List<List<Integer>> origin) {
        for (int i = 0; i < origin.size(); i++) {
            Integer from = origin.get(i).get(0);
            Integer to = origin.get(i).get(1);
            if (key == from && val != to && checkAcrossLine(val, from, to, new CopyOnWriteArrayList<>(origin)))
                return true;
        }
        return false;
    }

    /**
     * 递归检测同父节点不同子节点是否有交集
     */
    private boolean checkAcrossLine(Integer checkVal, Integer key, Integer val, List<List<Integer>> origin) {
        if (checkVal == key) return true;
        for (int i = 0; i < origin.size(); i++) {
            Integer from = origin.get(i).get(0);
            Integer to = origin.get(i).get(1);
            if (from == key && to == val) {
                origin.remove(i);
                continue;
            }
            if (to == val && checkAcrossLine(checkVal, from, to, origin)) return true;
        }
        return false;
    }
}
