# TSLping - Minecraft服务器延迟查询插件

## 📋 项目简介
TSLping 是一个专为 Luminol 服务端（Folia分支）设计的 Minecraft 插件，用于查询和显示玩家的网络延迟信息。

## ✨ 功能特性
- 🎯 查看指定玩家的延迟
- 📊 查看全服玩家延迟排行榜（分页显示）
- 🎨 彩色延迟显示（绿色<100ms，黄色100-200ms，红色>200ms）
- 📄 支持点击翻页按钮
- ⚙️ 完整的配置文件支持
- 🔐 权限控制系统

## 🎮 命令使用

### 基础命令
| 命令 | 描述 | 权限 |
|------|------|------|
| `/tping <玩家名>` | 查看指定玩家的延迟 | `tslping.use` |
| `/tping all [页码]` | 查看所有在线玩家的延迟排行 | `tslping.all` |
| `/tping reload` | 重载插件配置 | `tslping.reload` |

### 命令别名
- `/ping`
- `/tp`

## 🔐 权限系统
| 权限节点 | 描述 | 默认权限 |
|----------|------|----------|
| `tslping.use` | 使用基础延迟查询命令 | 所有玩家 |
| `tslping.all` | 查看全服延迟排行榜 | 仅OP |
| `tslping.reload` | 重载插件配置 | 仅OP |

## ⚙️ 配置文件说明

### config.yml 配置项
```yaml
messages:
  ping_format: "&a{player}: &f{ping}ms"           # 延迟显示格式
  page_header: "&6在线玩家 Ping 排行 - 第 {page}/{total_pages} 页"
  no_permission: "&c你没有权限执行此命令。"
  player_not_found: "&c找不到玩家: {player}"

settings:
  entries_per_page: 10    # 每页显示的玩家数量
  ping_colors:
    green: 100           # 绿色延迟阈值 (< 100ms)
    yellow: 200          # 黄色延迟阈值 (100-200ms)
                         # 红色延迟 (> 200ms)

pagination:
  previous_button: "&c[← 上一页]"
  next_button: "&a[下一页 →]"
```

## 🏗️ 项目结构
```
TSLping/
├── pom.xml                              # Maven配置文件
├── src/main/
│   ├── java/com/zvbj/tSLping/
│   │   ├── TSLping.java                 # 插件主类
│   │   ├── commands/PingCommand.java    # 命令处理器
│   │   ├── config/ConfigManager.java    # 配置管理器
│   │   ├── handlers/PingHandler.java    # 延迟数据处理
│   │   ├── pagination/PingPaginator.java # 分页逻辑
│   │   └── utils/TextUtils.java         # 文本工具类
│   └── resources/
│       ├── plugin.yml                   # 插件声明文件
│       └── config.yml                   # 默认配置文件
└── README.md                            # 项目说明文档
```

## 🔧 开发环境要求
- Java 21+
- Bukkit/Spigot/Paper API 1.21+
- Maven 3.6+

## 📦 构建说明
```bash
# 克隆项目
git clone <repository-url>
cd TSLping

# 编译打包
mvn clean package

# 生成的jar文件位于 target/ 目录
```

## 🚀 安装使用
1. 将编译好的 `TSLping-1.0.jar` 放入服务器的 `plugins` 目录
2. 重启服务器或使用 `/reload` 命令
3. 插件将自动生成默认配置文件
4. 根据需要修改 `plugins/TSLping/config.yml` 配置文件

## 📝 使用示例

### 查看单个玩家延迟
```
/tping Steve
# 输出: Steve: 45ms (绿色显示)
```

### 查看延迟排行榜
```
/tping all
# 显示第1页，包含延迟最低的10名玩家
# 支持点击按钮翻页

/tping all 2
# 直接跳转到第2页
```

### 重载配置
```
/tping reload
# 重新加载配置文件，无需重启服务器
```

## 🎨 功能截图说明
- ✅ 延迟数值根据阈值显示不同颜色
- ✅ 分页显示支持点击按钮翻页
- ✅ 实时获取最新延迟数据
- ✅ 完整的错误提示和权限控制

## 🔄 版本更新日志

### v1.0 (初始版本)
- 实现基础延迟查询功能
- 添加分页延迟排行榜
- 支持配置文件自定义
- 完整的权限控制系统
- 彩色延迟显示

## 🤝 贡献指南
欢迎提交 Issue 和 Pull Request 来帮助改进这个项目！

## 📄 许可证
本项目采用 MIT 许可证，详情请查看 LICENSE 文件。

---
**作者**: Zvbj  
**版本**: 1.0  
**兼容**: Luminol服务端 (Folia分支) + Paper API 1.21+
