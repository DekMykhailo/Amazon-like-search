
create table if not exists public.brands (
  id bigserial primary key,
  name text not null,
  created_at timestamptz not null default now(),
  constraint brands_name_uniq unique (name)
);

create table if not exists public.categories (
  id bigserial primary key,
  name text not null,
  created_at timestamptz not null default now(),
  constraint categories_name_uniq unique (name)
);

create table if not exists public.products (
  id text primary key,
  name text not null,
  brand_id bigint null references public.brands(id) on delete set null,
  image text null,
  created_at timestamptz not null default now()
);

create table if not exists public.product_categories (
  product_id text not null references public.products(id) on delete cascade,
  category_id bigint not null references public.categories(id) on delete cascade,
  created_at timestamptz not null default now(),
  primary key (product_id, category_id)
);
