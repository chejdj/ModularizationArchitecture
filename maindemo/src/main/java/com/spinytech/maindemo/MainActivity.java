package com.spinytech.maindemo;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.spinytech.macore.MaApplication;
import com.spinytech.macore.router.LocalRouter;
import com.spinytech.macore.router.RouterRequest;
import com.spinytech.macore.router.RouterResponse;

public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_local_sync_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    RouterResponse response = LocalRouter
                            .getInstance(MaApplication.getMaApplication())
                            .route(MainActivity.this,
                                    RouterRequest.obtain(MainActivity.this).provider("main")
                                            .action("sync")
                                            .data("1", "Hello")
                                            .data("2", "World"));
                    Toast.makeText(MainActivity.this, response.get(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.main_local_async_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final RouterResponse response = LocalRouter
                            .getInstance(MaApplication.getMaApplication())
                            .route(MainActivity.this,
                                    RouterRequest.obtain(MainActivity.this).provider("main")
                                            .action("async")
                                            .data("1", "Hello")
                                            .data("2", "World"));
                    Toast.makeText(MainActivity.this, "please wait", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final String result = response.get();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(MainActivity.this, result,
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.main_play_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long startTime = System.currentTimeMillis();
                try {
                    final RouterResponse response = LocalRouter
                            .getInstance(MaApplication.getMaApplication())
                            .route(MainActivity.this, RouterRequest.obtain(MainActivity.this)
                                    .domain("com.spinytech.maindemo:music")
                                    .provider("music")
                                    .action("play"));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final long time = System.currentTimeMillis() - startTime;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(MainActivity.this,
                                                    "async:" + response.isAsync() + " cost:" + time
                                                            + " response:" + response.get(),
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.main_stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long startTime = System.currentTimeMillis();
                try {
                    final RouterResponse response = LocalRouter
                            .getInstance(MaApplication.getMaApplication())
                            .route(MainActivity.this, RouterRequest.obtain(MainActivity.this)
                                    .domain("com.spinytech.maindemo:music")
                                    .provider("music")
                                    .action("stop"));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final long time = System.currentTimeMillis() - startTime;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(MainActivity.this,
                                                    "async:" + response.isAsync() + " cost:" + time
                                                            + " response:" + response.get(),
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.main_music_shutdown_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long startTime = System.currentTimeMillis();
                try {
                    final RouterResponse response = LocalRouter
                            .getInstance(MaApplication.getMaApplication())
                            .route(MainActivity.this, RouterRequest.obtain(MainActivity.this)
                                    .domain("com.spinytech.maindemo:music")
                                    .provider("music")
                                    .action("shutdown"));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final long time = System.currentTimeMillis() - startTime;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(MainActivity.this,
                                                    "async:" + response.isAsync() + " cost:" + time
                                                            + " response:" + response.get(),
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.main_wide_shutdown_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalRouter.getInstance(MaApplication.getMaApplication()).stopWideRouter();
            }
        });
        findViewById(R.id.main_pic_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long startTime = System.currentTimeMillis();
                try {
                    final RouterResponse response = LocalRouter
                            .getInstance(MaApplication.getMaApplication())
                            .route(MainActivity.this, RouterRequest.obtain(MainActivity.this)
                                    .domain("com.spinytech.maindemo:pic")
                                    .provider("pic")
                                    .action("pic")
                                    .data("is_big", "0"));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final long time = System.currentTimeMillis() - startTime;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(MainActivity.this,
                                                    "async:" + response.isAsync() + " cost:" + time
                                                            + " response:" + response.get(),
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.main_big_pic_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long startTime = System.currentTimeMillis();
                try {
                    final RouterResponse response = LocalRouter
                            .getInstance(MaApplication.getMaApplication())
                            .route(MainActivity.this, RouterRequest.obtain(MainActivity.this)
                                    .domain("com.spinytech.maindemo:pic")
                                    .provider("pic")
                                    .action("pic")
                                    .data("is_big", "1"));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final long time = System.currentTimeMillis() - startTime;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(MainActivity.this,
                                                    "async:" + response.isAsync() + " cost:" + time
                                                            + " response:" + response.get(),
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.main_web_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LocalRouter.getInstance(MaApplication.getMaApplication())
                            .route(MainActivity.this, RouterRequest.obtain(MainActivity.this)
                                    .provider("web")
                                    .action("web")
                            );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.main_attach_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    RouterResponse response = LocalRouter
                            .getInstance(MaApplication.getMaApplication())
                            .route(MainActivity.this, RouterRequest.obtain(MainActivity.this)
                                    .provider("main")
                                    .action("attachment")
                                    .object(findViewById(
                                            R.id.main_attach_btn))
                            );
                    if (response.getObject() instanceof Toast) {
                        ((Toast) response.getObject()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.main_get_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final RouterResponse response = LocalRouter.getInstance(
                            MaApplication.getMaApplication())
                            .route(MainActivity.this,
                                    RouterRequest.obtain(MainActivity.this).provider(
                                            "network").action("get"));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final String resultData = response.getData();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, resultData,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.main_post_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final RouterResponse response = LocalRouter.getInstance(
                            MaApplication.getMaApplication())
                            .route(MainActivity.this,
                                    RouterRequest.obtain(MainActivity.this).provider(
                                            "network").action("query"));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final String resultData = response.getData();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, resultData,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
