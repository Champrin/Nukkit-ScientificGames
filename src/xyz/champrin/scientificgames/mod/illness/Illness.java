package xyz.champrin.scientificgames.mod.illness;

import cn.nukkit.Player;
import xyz.champrin.scientificgames.libs.SGPlayer;

public class Illness {

    /*

多种病是可以叠加的 得了病吃错药 有惩罚

口腔溃疡 中毒(怪物抚摸,吃错东西) 破伤风 骨折

一、感冒和发烧一般是因感染病毒或是细菌而引起的。

二、而胃痛通常是人体消化系统出了问题的体现。

三、引起高血压的原因大致如下:
1.过度肥胖，是最常见的;
2.血液干涸；
3.脑血管疾病;
4.先天或是后天的心脏方面的缺失;
5.血液中的负离子含量太少;
6.日常摄入的盐太多；
7.精神紧张，压力过大；
8.缺钙;
9.家族遗传。

四、高血脂则是因为摄入过量的高胆固醇和高饱和脂肪酸引起的。

五、心肌梗塞是因冠状动脉持续、急性缺血缺氧而引起的心肌坏死。

六、冠心病最为复杂，是因胆固醇类脂质沉积在冠状动脉内膜壁下导致细胞结构组织增生，引起管腔狭窄从而发病的
当背包中的物品重量超过了你所能承受的最大重量后
你无法跳跃.

体力

体力可以在你奔跑,游泳,潜行,跳跃中减少,当你静止不动时,体力会自动恢复

*/
    public void getCold(SGPlayer sgplayer){
        Player player = sgplayer.getPlayer();
        sgplayer.illness.add("感冒");
    }
    public void getFever(SGPlayer sgplayer){
        Player player = sgplayer.getPlayer();
        sgplayer.illness.add("发烧");
    }
    public void getStomachache(SGPlayer sgplayer){
        Player player = sgplayer.getPlayer();
        sgplayer.illness.add("胃痛");
    }
    public void getGXY(SGPlayer sgplayer){
        Player player = sgplayer.getPlayer();
        sgplayer.illness.add("高血压");
    }
    public void getGXZ(SGPlayer sgplayer){
        Player player = sgplayer.getPlayer();
        sgplayer.illness.add("高血脂");
    }
    public void getXJGS(SGPlayer sgplayer){
        Player player = sgplayer.getPlayer();
        sgplayer.illness.add("心肌梗塞");
    }
    public void getGXB(SGPlayer sgplayer){
        Player player = sgplayer.getPlayer();
        sgplayer.illness.add("冠心病");
    }
}
