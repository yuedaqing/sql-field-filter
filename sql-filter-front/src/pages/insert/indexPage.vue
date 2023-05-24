<template>
  <div id="page-style">
    <a-form
      :label-col="{ span: 8 }"
      :wrapper-col="{ span: 16 }"
      :model="formState"
      @finish="onFinish"
    >
      <a-form-item
        label="目标字段"
        name="field1"
        :wrapper-col="{ span: 8 }"
        :rules="[{ required: true, message: '输入目标字段!' }]"
      >
        <a-input v-model:value="formState.field1" placeholder="输入目标字段" />
      </a-form-item>
      <a-form-item
        label="更改字段"
        name="field2"
        :wrapper-col="{ span: 8 }"
        :rules="[{ required: true, message: '输入更改字段!' }]"
      >
        <a-input v-model:value="formState.field2" placeholder="输入更改字段" />
      </a-form-item>
      <a-form-item
        :wrapper-col="{ span: 8 }"
        label="主键字段"
        name="idField"
        :rules="[{ required: true, message: '输入主键字段!' }]"
      >
        <a-input v-model:value="formState.idField" placeholder="输入主键字段" />
      </a-form-item>
      <a-form-item :wrapper-col="{ span: 16 }" label="sql文件">
        <a-upload
          :max-count="1"
          :before-upload="beforeUpload"
          @change="handleUpload"
          @remove="removeFile"
        >
          <a-button>
            <UploadOutlined />
            Select File
          </a-button>
        </a-upload>
      </a-form-item>
      <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
        <a-button
          type="primary"
          :disabled="selectedFile == null"
          :loading="uploading"
          style="margin-top: 16px"
          html-type="submit"
        >
          {{ uploading ? "Uploading" : "Start Upload" }}
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import { message, UploadProps } from "ant-design-vue";
import { postFile } from "@/plugins/axios";
import { UploadOutlined } from "@ant-design/icons-vue";

const uploading = ref<boolean>(false);

interface FormState {
  field1: string;
  field2: string;
  idField: string;
}

const formState = ref<FormState>({
  field1: "",
  field2: "",
  idField: "",
});

const selectedFile = ref<File | null>(null);
/**
 * 上传前校验文件的大小
 * @param file
 */
const beforeUpload: UploadProps["beforeUpload"] = (file) => {
  console.log("beforeUpload:", file);
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    message.error("File must be smaller than 2MB!");
  }
  return false;
};

/**
 * 监听文件是否上传
 * @param info
 */
const handleUpload = (info: any) => {
  selectedFile.value = info.file;
  console.log("文件接值了:", selectedFile.value);
};
// res 是返回的文件流，type 是文件MIME类型, fileName 是给下载的文件一个名称
const blobDownloadFile = (res: any, type: string, fileName: string) => {
  const blob = new Blob([res], {
    type: type,
  });
  const a = document.createElement("a");
  const URL = window.URL || window.webkitURL;
  const herf = URL.createObjectURL(blob);
  a.href = herf;
  a.download = fileName;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  window.URL.revokeObjectURL(herf);
};
/**
 * 上传文件
 */
const onFinish = async () => {
  console.log(selectedFile.value);
  if (!selectedFile.value) {
    message.error("请上传一个SQL文件");
    return;
  }
  try {
    const data = await postFile(
      "/sql/filter/insert",
      {
        file: selectedFile.value,
        field1: formState.value.field1,
        field2: formState.value.field2,
        idField: formState.value.idField,
      },
      {
        responseType: "blob",
        headers: {
          "Content-Type": "multipart/form-data",
        },
      }
    );
    blobDownloadFile(data, "text/plain", "SQL文件");
    message.success("新的SQL生成啦！");
    console.log(data);
  } catch (error) {
    message.error("文件导入失败");
    console.error(error);
  }
  selectedFile.value = null;
};
const removeFile = () => {
  selectedFile.value = null;
  return true;
};
</script>
<style scoped>
#page-style {
  margin-top: 50px;
}
</style>
