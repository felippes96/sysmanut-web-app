package sysmanut.infra;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * Parâmetros de conexão com o banco. Substitui as constantes (inclusive a senha)
 * que ficavam gravadas no código-fonte de ConexaoMySQL.
 *
 * Ordem de prioridade: variáveis de ambiente &gt; arquivo db.properties &gt; padrão (url/usuário).
 * A senha NÃO tem valor padrão: se não for informada, a aplicação falha com mensagem clara.
 */
public final class ConfiguracaoBanco {

    public static final String ENV_URL = "SYSMANUT_DB_URL";
    public static final String ENV_USUARIO = "SYSMANUT_DB_USER";
    public static final String ENV_SENHA = "SYSMANUT_DB_PASSWORD";
    public static final String ARQUIVO_PADRAO = "db.properties";

    private static final String URL_PADRAO = "jdbc:mysql://localhost:3306/sysmanut";
    private static final String USUARIO_PADRAO = "root";

    private final String url;
    private final String usuario;
    private final String senha;

    private ConfiguracaoBanco(String url, String usuario, String senha) {
        this.url = url;
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getUrl() {
        return url;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }

    /** Carrega do ambiente real e do arquivo db.properties (se existir) no diretório atual. */
    public static ConfiguracaoBanco carregar() {
        return montar(System.getenv(), lerArquivo(Paths.get(ARQUIVO_PADRAO)));
    }

    /** Monta a configuração a partir de fontes explícitas (facilita testes). */
    public static ConfiguracaoBanco montar(Map<String, String> ambiente, Properties arquivo) {
        String url = escolher(ambiente.get(ENV_URL), arquivo.getProperty("db.url"), URL_PADRAO);
        String usuario = escolher(ambiente.get(ENV_USUARIO), arquivo.getProperty("db.user"), USUARIO_PADRAO);
        String senha = escolher(ambiente.get(ENV_SENHA), arquivo.getProperty("db.password"), null);

        if (senha == null) {
            throw new IllegalStateException("Senha do banco não configurada. Defina a variável de ambiente "
                    + ENV_SENHA + " ou crie o arquivo " + ARQUIVO_PADRAO + " (veja db.properties.example).");
        }
        return new ConfiguracaoBanco(url, usuario, senha);
    }

    private static String escolher(String ambiente, String arquivo, String padrao) {
        if (ambiente != null && !ambiente.isEmpty()) {
            return ambiente;
        }
        if (arquivo != null && !arquivo.isEmpty()) {
            return arquivo;
        }
        return padrao;
    }

    private static Properties lerArquivo(Path caminho) {
        Properties props = new Properties();
        if (Files.isRegularFile(caminho)) {
            try (InputStream in = Files.newInputStream(caminho)) {
                props.load(in);
            } catch (IOException e) {
                throw new IllegalStateException("Não foi possível ler " + caminho + ": " + e.getMessage(), e);
            }
        }
        return props;
    }
}
